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
 *     $Date: 2009-07-03 20:57:52 +0200 (Fri, 03 Jul 2009) $
 *   $Author: bananeweizen $
 * $Revision: 5786 $
 */

package tvbrowser.core.filters;

import devplugin.Marker;
import devplugin.ProgramFilter;

public class PluginFilter implements ProgramFilter {


  private static final util.ui.Localizer mLocalizer
          = util.ui.Localizer.getLocalizerFor(PluginFilter.class);


  public boolean accept(devplugin.Program prog) {
    final Marker[] markerArr = prog.getMarkerArr();
    return (markerArr!=null && markerArr.length>0);
  }

  public String getName() {
      return toString();
  }

  public String toString() {
    return mLocalizer.msg("pluginfFilterName","Marked programs");
  }


  public boolean containsRuleComponent(String comp) {
    return false;
  }
  
  public boolean equals(Object o) {
    if(o instanceof ProgramFilter) {
      return getClass().equals(o.getClass())
          && getName().equals(((ProgramFilter) o).getName());
    }
    
    return false;
  }



}