/*
 * TV-Browser
 * Copyright (C) 04-2003 Martin Oberhauser (martin_oat@yahoo.de)
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
 *     $Date: 2010-03-07 07:27:49 +0100 (Sun, 07 Mar 2010) $
 *   $Author: bananeweizen $
 * $Revision: 6542 $
 */
package util.misc;

import util.browserlauncher.Launch;

/**
 * Checks which OS is used.
 *
 * Uses the BrowserLauncher-Detection-Code
 *
 * @author bodum
 * @since 2.2
 */
public class OperatingSystem {
  /**
   * @return true, if the Operating System is Windows
   */
  public static boolean isWindows() {
    return Launch.getOs() == Launch.OS_WINDOWS;
  }

  /**
   * @return true, if the Operating System is MacOS
   */
  public static boolean isMacOs() {
    return Launch.getOs() == Launch.OS_MAC;
  }

  /**
   * @return <code>True</code>, if the Operation System is Linux.
   * @since 2.2.4/2.6
   */
  public static boolean isLinux() {
    return Launch.getOs() == Launch.OS_LINUX;
  }

  /**
   * @return true, if the Operating System is not Windows or MacOS
   */
  public static boolean isOther() {
    return Launch.getOs() == Launch.OS_OTHER;
  }

  /**
   * @return true, if the OS is Windows with 64 bit
   * @since 3.0
   */
  public static boolean isWindows64() {
    return isWindows() && System.getProperty("sun.arch.data.model").equals("64"); // this may or may not work
  }

  /**
   * @return true, if a KDE session is running
   * @since 3.0
   */
  public static boolean isKDE() {
    if (!isLinux()) {
      return false;
    }
    try {
      final String kdeSession = System.getenv("KDE_FULL_SESSION");
      if (kdeSession != null) {
        return kdeSession.compareToIgnoreCase("true") == 0;
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

}