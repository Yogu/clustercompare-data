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
 *     $Date: 2010-06-28 19:33:48 +0200 (Mon, 28 Jun 2010) $
 *   $Author: bananeweizen $
 * $Revision: 6662 $
 */
package util.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * Draws a Line
 * 
 * @since 2.2
 */
public class LineComponent extends JComponent {
  /**
   * Creates a Line-Drawing-Component
   * @param color Color of the Line
   */
  public LineComponent(Color color) {
    setForeground(color);
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    if (isOpaque()) {
      g.setColor(getBackground());
      g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    int y = getHeight() / 2;
    g.setColor(getForeground());
    g.drawLine(5, y, getWidth() - 10, y);
  }
}