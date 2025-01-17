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
 *     $Date: 2007-12-28 14:51:08 +0100 (Fri, 28 Dec 2007) $
 *   $Author: Bananeweizen $
 * $Revision: 4137 $
 */
package tvbrowser.core;

/**
 * A listener that listens for TV update events.
 * 
 * @author Til Schneider, www.murfman.de
 */
public interface TvDataUpdateListener {

  /**
   * Is called when a the TV data update has started.
   */
  public void tvDataUpdateStarted();

  /**
   * Is called when a the TV data update has finished.
   */
  public void tvDataUpdateFinished();

}
