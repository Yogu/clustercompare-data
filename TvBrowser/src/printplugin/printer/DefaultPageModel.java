/*
 * TV-Browser
 * Copyright (C) 04-2003 Martin Oberhauser (martin@tvbrowser.org)
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
 *     $Date: 2005-05-22 16:29:39 +0200 (Sun, 22 May 2005) $
 *   $Author: darras $
 * $Revision: 1228 $
 */

package printplugin.printer;


public class DefaultPageModel extends AbstractPageModel {

  private String mHeader;

  public DefaultPageModel(String header) {
    super();
    mHeader = header;
  }

  public DefaultPageModel() {
    this(null);
  }


	public String getHeader() {
		return mHeader;
	}
  
}