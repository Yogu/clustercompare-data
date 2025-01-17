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
 *     $Date: 2004-10-22 16:02:11 +0200 (Fri, 22 Oct 2004) $
 *   $Author: darras $
 * $Revision: 836 $
 */
package util.settings;

import util.io.IOUtilities;

/**
 * 
 * 
 * @author Til Schneider, www.murfman.de
 */
public class EncodedStringProperty extends Property {

  private long mSeed;
  private String mDefaultValue;
  private String mCachedValue;
  
  
  
  public EncodedStringProperty(PropertyManager manager, String key,
    String defaultValue, long seed)
  {
    super(manager, key);

    mDefaultValue = defaultValue;
    mCachedValue = null;
    mSeed = seed;
  }


  public String getDefault() {
    return mDefaultValue;
  }


  public String getString() {
    if (mCachedValue == null) {
      String encodedString = getProperty();
      if (encodedString == null) {
        mCachedValue = mDefaultValue;
      } else {
        mCachedValue = IOUtilities.xorDecode(encodedString, mSeed);
      }
    }

    return mCachedValue;
  }
  
  
  public void setString(String value) {
    if (value == null) {
      throw new IllegalArgumentException("You can't set a null value");
    }
    
    if (value.equals(mDefaultValue)) {
      setProperty(null);
    } else {
      setProperty(IOUtilities.xorEncode(value, mSeed));
    }
    
    mCachedValue = value;
  }
  
  
  protected void clearCache() {
    mCachedValue = null;
  }

}
