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

package tvbrowser.ui.settings.tablebackgroundstyles;

import javax.swing.JPanel;

import tvbrowser.core.Settings;
import tvbrowser.ui.settings.util.ColorButton;
import tvbrowser.ui.settings.util.ColorLabel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


/**
 * Created by: Martin Oberhauser (martin@tvbrowser.org)
 * Date: 30.04.2005
 * Time: 17:47:39
 */
public class SingleColorBackgroundStyle implements TableBackgroundStyle {

  private static final util.ui.Localizer mLocalizer
    = util.ui.Localizer.getLocalizerFor(SingleColorBackgroundStyle.class);
  
  private ColorLabel mColorLabel;

  public SingleColorBackgroundStyle() {

  }

  public boolean hasContent() {
    return true;
  }

  public JPanel createSettingsContent() {
    CellConstraints cc = new CellConstraints();
    PanelBuilder pb = new PanelBuilder(new FormLayout("default,5dlu,default,5dlu,default","default"));
    
    mColorLabel = new ColorLabel(Settings.propProgramTableBackgroundSingleColor.getColor());
    mColorLabel.setStandardColor(Settings.propProgramTableBackgroundSingleColor.getDefaultColor());
    
    ColorButton colorButton = new ColorButton(mColorLabel);
    
    pb.addLabel(mLocalizer.msg("text","Background color"), cc.xy(1,1));
    pb.add(mColorLabel, cc.xy(3,1));
    pb.add(colorButton, cc.xy(5,1));
    
    return pb.getPanel();
  }

  public void storeSettings() {
    Settings.propProgramTableBackgroundSingleColor.setColor(mColorLabel.getColor());
  }

  public String getName() {
    return mLocalizer.msg("style","Single color");
  }


  public String toString() {
    return getName();
  }

  public String getSettingsString() {
    return "singleColor";
  }

}
