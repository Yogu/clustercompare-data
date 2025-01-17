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
 *     $Date: 2005-05-05 19:35:33 +0200 (Thu, 05 May 2005) $
 *   $Author: troggan $
 * $Revision: 1178 $
 */
package util.ui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import devplugin.Channel;

/**
 * A Channel-TableCellRenderer
 * 
 * @author bodum
 */
public class ChannelTableCellRenderer extends DefaultTableCellRenderer {
  /** Internal reused ChannelLabel */
  private ChannelLabel mChannelLabel;

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column) {
    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    if (value instanceof Channel) {

      Channel channel = (Channel) value;

      if (mChannelLabel == null) {
        mChannelLabel = new ChannelLabel();
      }

      mChannelLabel.setChannel(channel);
      mChannelLabel.setOpaque(label.isOpaque());
      mChannelLabel.setForeground(label.getForeground());
      mChannelLabel.setBackground(label.getBackground());

      if ((mChannelLabel.getHeight()+2) > table.getRowHeight(row)) {
        table.setRowHeight(row, mChannelLabel.getHeight()+2);
      }

      label = mChannelLabel;
    }
    
    return label;
  }

}