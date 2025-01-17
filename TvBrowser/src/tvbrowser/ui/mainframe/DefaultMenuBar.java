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
 *     $Date: 2010-06-28 19:33:48 +0200 (Mon, 28 Jun 2010) $
 *   $Author: bananeweizen $
 * $Revision: 6662 $
 */

package tvbrowser.ui.mainframe;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * menu bar for non Mac systems
 *
 */
public class DefaultMenuBar extends MenuBar {

  public DefaultMenuBar(final MainFrame mainFrame, final JLabel label) {
    super(mainFrame, label);

    JMenu fileMenu = createMenu("menu.main", "&File");
    add(fileMenu);
    fileMenu.add(mQuitMI);

    createCommonMenus();

    mHelpMenu.addSeparator();
    mHelpMenu.add(mAboutMI);

    mQuitMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
    mSettingsMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit()
        .getMenuShortcutKeyMask()));
  }

  protected void setPluginMenuItems(JMenuItem[] items) {
    super.setPluginMenuItems(items);
    // on non Mac systems, the settings are in the tools menu
    mPluginsMenu.addSeparator();
    mPluginsMenu.add(mSettingsMI);
  }

}