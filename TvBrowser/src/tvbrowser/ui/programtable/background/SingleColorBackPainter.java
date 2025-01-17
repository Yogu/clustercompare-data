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
 *     $Date: 2008-02-25 19:48:19 +0100 (Mon, 25 Feb 2008) $
 *   $Author: ds10 $
 * $Revision: 4307 $
 */
package tvbrowser.ui.programtable.background;

import java.awt.Graphics;
import java.awt.Rectangle;

import tvbrowser.core.Settings;
import tvbrowser.ui.programtable.ProgramTableLayout;
import tvbrowser.ui.programtable.ProgramTableModel;

/**
 * 
 * 
 * @author Til Schneider, www.murfman.de
 */
public class SingleColorBackPainter extends AbstractBackPainter {

  /**
   * Paints the background.
   * 
   * @param grp
   * @param columnWidth
   * @param tableHeight
   * @param clipBounds
   * @param layout The table's layout
   * @param model The table model
   */
  public void paintBackground(Graphics grp, int columnWidth, int tableHeight,
    int minCol, int maxCol, Rectangle clipBounds, ProgramTableLayout layout,
    ProgramTableModel model)
  {
    grp.setColor(Settings.propProgramTableBackgroundSingleColor.getColor());
    grp.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
  }

}
