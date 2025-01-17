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
 *     $Date: 2007-09-01 12:51:17 +0200 (Sat, 01 Sep 2007) $
 *   $Author: ds10 $
 * $Revision: 3699 $
 */
package util.io;

/**
 * 
 * 
 * @author Til Schneider, www.murfman.de
 */
public class FileFormatException extends Exception {

  public FileFormatException(String msg) {
    super(msg);
  }

  public FileFormatException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
