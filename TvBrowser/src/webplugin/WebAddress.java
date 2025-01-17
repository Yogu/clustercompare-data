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
*     $Date: 2010-06-28 19:33:48 +0200 (Mon, 28 Jun 2010) $
*   $Author: bananeweizen $
* $Revision: 6662 $
*/
package webplugin;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;

import util.ui.ImageUtilities;
import devplugin.Plugin;

/**
 * a web address
 */
public final class WebAddress implements Cloneable {
  /** default icon for all web addresses */
  private static ImageIcon DEFAULT_ICON = null;

  /** URL */
  private String mUrl;

  /** Icon */
  private String mIconFileName;

  /** Name */
  private String mName;

  /** Entered by User ? */
  private boolean mUserEntry = false;

  /** Active ? */
  private boolean mActive = true;

  /**
   * cache image icon of this web address to avoid repeated file reads
   */
  private ImageIcon mIcon;

  /**
   * Create the Address
   *
   * @param name
   *          Name
   * @param url
   *          Url ( {0} as placeholder )
   * @param iconFile
   *          Icon-File
   * @param userEntry
   *          Is this Entry editable?
   * @param active
   *          Is this Entry active?
   */
  public WebAddress(String name, String url, String iconFile, boolean userEntry, boolean active) {
    mName = name;
    mIconFileName = iconFile;
    mUrl = url;
    mUserEntry = userEntry;
    mActive = active;
  }

  /**
   * Create a Copy of a WebAddress
   *
   * @param address Copy Settings from this WebAddress
   */
  public WebAddress(WebAddress address) {
    mName = address.getName();
    mIconFileName = address.getIconFile();
    mUrl = address.getUrl();
    mUserEntry = address.isUserEntry();
    mActive = address.isActive();
  }

  /**
   * Create a WebAddress with a Stream
   * @param in Input-Stream
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public WebAddress(ObjectInputStream in) throws IOException, ClassNotFoundException {
    readData(in);
  }

  public void setName(String name) {
    mName = name;
  }

  public String getName() {
    return mName;
  }

  public void setUrl(String url) {
    mUrl = url;
  }

  public String getUrl() {
    return mUrl;
  }

  public void setIconFile(String iconFile) {
    mIconFileName = iconFile;
    // reset currently loaded icon
    mIcon = null;
  }

  public String getIconFile() {
    return mIconFileName;
  }

  public Icon getIcon() {
    if (mIcon != null) {
      return mIcon;
    }

    if (DEFAULT_ICON == null) {
      DEFAULT_ICON = WebPlugin.getInstance().createImageIcon("actions", "web-search", 16);
    }

    if (StringUtils.isEmpty(mIconFileName)) { return DEFAULT_ICON; }

    try {
      StringBuilder filePath = new StringBuilder(Plugin.getPluginManager()
          .getTvBrowserSettings().getTvBrowserUserHome());
      filePath.append(File.separator).append("WebFavIcons").append(File.separator).append(mIconFileName);

      mIcon = new ImageIcon(ImageUtilities.createImageAsynchronous(filePath.toString()));
      if ((mIcon != null) && (mIcon.getIconWidth() > 0)) {
        return mIcon;
      }
    } catch (Exception e) {
    }

    mIcon = null;
    return DEFAULT_ICON;
  }

  public boolean isActive() {

    if (StringUtils.isBlank(mUrl)) { return false; }

    if (StringUtils.isBlank(mName)) { return false; }

    return mActive;
  }

  public void setActive(boolean active) {
    mActive = active;
  }

  public boolean isUserEntry() {
    return mUserEntry;
  }

  public void setUserEntry(boolean user) {
    mUserEntry = user;
  }

  public String toString() {
    return mName;
  }

  public Object clone() {
    return new WebAddress(this);
  }

  public void readData(ObjectInputStream in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    mName = (String) in.readObject();
    mIconFileName = (String) in.readObject();

    if(version  == 2 && mIconFileName != null) {
      File iconFile = new File(mIconFileName);

      if(iconFile.isFile()) {
        mIconFileName = iconFile.getName();
      } else {
        mIconFileName = "";
      }
    }

    mUrl = (String) in.readObject();

    if (version == 1) {
      String encoding = (String) in.readObject();
      mUrl = mUrl.replaceAll("\\{0\\}", "{urlencode(title, \""+encoding+"\")}");
    }
    mUserEntry = in.readBoolean();
    mActive = in.readBoolean();
  }

  public void writeData(ObjectOutputStream out) throws IOException {
    out.writeInt(3);

    out.writeObject(mName);
    out.writeObject(mIconFileName);
    out.writeObject(mUrl);
    out.writeBoolean(mUserEntry);
    out.writeBoolean(mActive);
  }
}