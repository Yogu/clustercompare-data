/*
 * TV-Browser
 * Copyright (C) 04-2003 Martin Oberhauser (darras@users.sourceforge.net)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * CVS information:
 *  $RCSfile$
 *   $Source$
 *     $Date: 2010-06-28 19:33:48 +0200 (Mon, 28 Jun 2010) $
 *   $Author: bananeweizen $
 * $Revision: 6662 $
 */
package util.paramhandler;

import java.awt.Window;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import util.ui.Localizer;
import devplugin.Program;


/**
 *  The ParamParser analyzes a String and calls a ParamLibrary for each
 *  Key/Function it finds.
 *
 *  This System is easily extendible. For an example look into the CapturePlugin code
 *
 *  @author bodum
 */
public class ParamParser {
  /** The Library to use */
  private ParamLibrary mLibrary;
  /** The ErrorString */
  private String mErrorString = "";
  /** This is true, if an error occurred*/
  private boolean mErrors = false;

  /**
   * Create a ParamParser with the default ParamLibrary
   */
  public ParamParser() {
    mLibrary = new ParamLibrary();
  }

  /**
   * Create a ParamParser with a specific Library
   * @param lib ParamLibrary to use
   */
  public ParamParser(ParamLibrary lib) {
    mLibrary = lib;
  }

  /**
   * Returns True if an error occurred
   * @return True if Error
   */
  public boolean hasErrors() {
    return mErrors;
  }

  /**
   * The Error-String for Details about an Error
   * @return Error-String
   */
  public String getErrorString() {
    return mErrorString;
  }

  /**
   * The used ParamLibrary
   * @return ParamLibrary used in this Parser
   */
  public ParamLibrary getParamLibrary() {
    return mLibrary;
  }

  /**
   * Set the ParamLibrary that this Parser uses
   * @param lib ParamLibrary to use
   */
  public void setParamLibrary(ParamLibrary lib) {
    mLibrary = lib;
  }

  /**
   * Analyze a String and return the parsed String.
   *
   * If an error occurred, the return value is null.
   *
   * @param command String to parse
   * @param prg Program to use while parsing
   * @return parsed String, null if an error occurred
   */
  public String analyse(String command, Program prg) {
    boolean escapemode = false;
    boolean commandmode = false;
    boolean stringmode = false;
    StringBuilder cmdBuffer = new StringBuilder();
    char[] chars = command.toCharArray();
    StringBuilder ret = new StringBuilder();

    for (int pos=0;pos<chars.length;pos++) {

      if (escapemode) {
        ret.append(chars[pos]);
        escapemode = false;
      } else if (chars[pos] == '\\'){
        escapemode = true;
      } else if (commandmode && (chars[pos] == '"')) {
        cmdBuffer.append(chars[pos]);
        stringmode = !stringmode;
      } else if (stringmode && commandmode) {
        cmdBuffer.append(chars[pos]);
      } else if (chars[pos] == '{'){
        commandmode = true;
        cmdBuffer = new StringBuilder();
      } else if (chars[pos] == '}'){
        String newCommand = cmdBuffer.toString().trim();

        String retu = analyseCommand(prg, newCommand, pos-cmdBuffer.length());

        if (retu == null) {
          return null;
        }

        ret.append(retu);

        commandmode = false;
      } else if (commandmode) {
        cmdBuffer.append(chars[pos]);
      } else {
        ret.append(chars[pos]);
      }

    }

    if (commandmode) {
      setError("One \"{\" was not closed properly");
      return null;
    }

    return ret.toString();
  }

  private void setError(final String message) {
    mErrors = true;
    mErrorString = message;
  }

  /**
   * Analyze a command and calls the Functions in the ParamLibrary
   *
   * @param prg Program to use
   * @param newCommand Command to analyze
   * @param pos Pos of Command in String
   * @return null if error, otherwise result
   */
  private String analyseCommand(Program prg, String newCommand, int pos) {
    String ret;

    if (newCommand.startsWith("\"") && newCommand.endsWith("\"")) {
      ret = newCommand.substring(1, newCommand.length()-1);
    } else if (newCommand.indexOf('(') > -1) {
      String funcRet = parseFunction(prg, newCommand, pos);
      if (funcRet == null) {
        return null;
      }
      ret = funcRet;
    } else {
      String cmdRet = mLibrary.getStringForKey(prg, newCommand.trim());

      if ((cmdRet == null) && (!mLibrary.hasErrors())) {
        setError("Could not understand Param \""+newCommand+"\" at Position "+ (pos) + ".");
        return null;
      } else if (cmdRet == null) {
        mErrors = mLibrary.hasErrors();
        mErrorString = mLibrary.getErrorString();
        return null;
      }

      ret = cmdRet;
    }

    return ret;
  }

  /**
   * This Function analyzes Functions
   *
   * @param prg Program to use
   * @param function found Function
   * @param pos Pos of Command in String
   * @return null if error, otherwise result of Function
   */
  private String parseFunction(Program prg, String function, int pos) {

    String funcname = StringUtils.substringBefore(function,"(").trim();

    if (StringUtils.isEmpty(funcname)) {
      setError("A ( without a function-name was found at Position " + pos);
      return null;
    }

    if (!function.endsWith(")")) {
      setError("Function-Call \""+funcname+"\" doesn't end with \")\" at Position " + pos);
      return null;
    }

    String params = function.substring(function.indexOf('(') + 1, function
        .lastIndexOf(')'));

    String[] splitparams = splitParams(params, pos);

    if(splitparams == null) {
      return null;
    }

    for (int i=0;i<splitparams.length;i++) {
      splitparams[i] = analyseCommand(prg, splitparams[i], pos);

      if (splitparams[i] == null) {
        return null;
      }
    }

    String result = mLibrary.getStringForFunction(prg, funcname, splitparams);

    if (result == null) {
      mErrors = mLibrary.hasErrors();
      mErrorString = mLibrary.getErrorString();
    }

    return result;
  }

  /**
   * Splits a String into the separate parameters
   * @param params String to split
   * @param strpos Pos in String (used for Error-Display)
   * @return split String
   */
  private String[] splitParams(String params, int strpos) {
    int infunction = 0;
    boolean instring = false;
    StringBuilder curparam = new StringBuilder();

    ArrayList<String> list = new ArrayList<String>();

    char[] chars = params.toCharArray();

    for (char c : chars) {

      if (c == '"') {
        instring = !instring;
        curparam.append(c);
      } else if (c == ')') {
        infunction--;
        curparam.append(c);
      } else if (c == '(') {
        infunction++;
        curparam.append(c);
      } else if ((infunction>0)||instring) {
        curparam.append(c);
      } else if (c == ',') {
        list.add(curparam.toString().trim());
        curparam = new StringBuilder();
      } else {
        curparam.append(c);
      }

      if (infunction<0) {
        setError("One \")\" at the wrong Position found");
        return null;
      }
    }

    if (instring) {
      setError("One \" was not closed properly");
      return null;
    } else if (infunction>0) {
      setError("One \"(\" was not closed properly");
      return null;
    }

    list.add(curparam.toString().trim());
    return list.toArray(new String[list.size()]);
  }

  /**
   * show the parser error(s), if there were errors during parsing
   * @param parent parent window
   * @return <code>true</code>, if an error exists in the parser, <code>false</code> otherwise
   * @since 3.0
   */
  public boolean showErrors(Window parent) {
    if (hasErrors()) {
      JOptionPane.showMessageDialog(parent, getErrorString(), Localizer.getLocalization(Localizer.I18N_ERROR), JOptionPane.ERROR_MESSAGE);
      return true;
    }
    return false;
  }

  /**
   * show the parser error(s), if there were errors during parsing
   * @return <code>true</code>, if an error exists in the parser, <code>false</code> otherwise
   * @since 3.0
   */
  public boolean showErrors() {
    return showErrors(null);
  }
}