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
 *     $Date: 2010-01-26 18:30:19 +0100 (Tue, 26 Jan 2010) $
 *   $Author: bananeweizen $
 * $Revision: 6407 $
 */

package devplugin;

import javax.swing.Icon;
import javax.swing.JPanel;

/**
 * Represents a page in the settings dialog.
 *
 * If you want to detect a cancel pressed action, you should use {@link CancelableSettingsTab}
 *
 * @author Martin Oberhauser
 */
public interface SettingsTab {
  
  /**
   * Creates the settings panel for this tab.
   */
  public JPanel createSettingsPanel();

  
  /**
   * Called by the host-application, if the user wants to save the settings.
   */
  public void saveSettings();

  
  /**
   * Returns the icon of the tab-sheet.
   * <br>
   * For plugins the plugin icon is used if this method returns null.
   */
  public Icon getIcon();
  
  
  /**
   * Returns the title of the tab-sheet.
   * <br>
   * For plugins the return value of this method is ignored since version 2.7
   */
  public String getTitle();

}
