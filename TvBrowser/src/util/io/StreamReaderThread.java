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

package util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A class that reads from InputStreams in a Thread.
 *
 * @author Ren� Mach
 *
 */
public class StreamReaderThread extends Thread {

  private InputStream mInput;
  private boolean mSaveOutput;
  private StringBuffer mOutput;
  private String mEncoding;

  /**
   * @param stream
   *          The InputStream to read from.
   * @param save
   *          Save the output of the stream.
   */
  public StreamReaderThread(InputStream stream, boolean save) {
    this(stream, save, null);
  }

  /**
   * @param stream
   *          The InputStream to read from.
   * @param save
   *          Save the output of the stream.
   * @param encoding
   *          Encoding of the Stream
   */
  public StreamReaderThread(InputStream stream, boolean save, String encoding) {
    super("Stream reader");
    mInput = stream;
    mSaveOutput = save;
    mOutput = new StringBuffer();
    mEncoding = encoding;
  }

  public void run() {
    try {
      String line;

      BufferedReader reader;

      if (mEncoding != null) {
        reader = new BufferedReader(new InputStreamReader(mInput, mEncoding));
      } else {
        reader = new BufferedReader(new InputStreamReader(mInput));
      }

      while ((line = reader.readLine()) != null) {
        if (mSaveOutput) {
          mOutput.append(line).append('\n');
        }
      }

    } catch (IOException e) {}
  }

  /**
   * @return The output of the stream.
   * @deprecated since 3.0, use {@link ExecutionHandler#getOutput()} instead
   */
  @Deprecated
  public String getOutput() {
    return mOutput.toString();
  }

  protected String getOutputString() {
    return mOutput.toString();
  }
}
