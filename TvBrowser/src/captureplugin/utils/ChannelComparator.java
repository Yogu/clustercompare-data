/*
 * CapturePlugin by Andreas Hessel (Vidrec@gmx.de), Bodo Tasche
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
 *     $Date: 2009-09-27 22:30:36 +0200 (Sun, 27 Sep 2009) $
 *   $Author: bananeweizen $
 * $Revision: 5980 $
 */
package captureplugin.utils;

import java.util.Comparator;

import devplugin.Channel;
/**
 * Compares two Channels
 * 
 * @author bodum
 */
public class ChannelComparator implements Comparator<Channel> {

  /**ChannelComparatorwo channels
   */
  public int compare(Channel a, Channel b) {

    int value = a.getDataServiceProxy().getInfo().getName().compareTo(
        b.getDataServiceProxy().getInfo().getName());

    if (value == 0) {
      value = a.getId().compareTo(b.getId());
    }

    return value;
  }

}
