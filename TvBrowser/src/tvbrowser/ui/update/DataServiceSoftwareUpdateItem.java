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
 * SVN information:
 *       $Id: DataServiceSoftwareUpdateItem.java 5848 2009-08-01 22:55:49Z ds10 $
 *     $Date: 2009-08-02 00:55:49 +0200 (Sun, 02 Aug 2009) $
 *   $Author: ds10 $
 * $Revision: 5848 $
 */
package tvbrowser.ui.update;

/**
 * Contains the update information for a devplugin.AbstractTvDataService
 * 
 * @author Ren� Mach
 */
public class DataServiceSoftwareUpdateItem extends PluginsSoftwareUpdateItem {
  
  /**
   * Creates an instance of this class.
   * 
   * @param name The class name of the dataservice.
   */
  public DataServiceSoftwareUpdateItem(String name) {
    super(name);
  }
}
