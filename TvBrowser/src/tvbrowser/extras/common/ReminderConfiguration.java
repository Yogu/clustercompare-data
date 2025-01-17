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

package tvbrowser.extras.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReminderConfiguration {

  public static final String REMINDER_DEFAULT = "window";
  public static final String REMINDER_EMAIL = "email-reminder";
  public static final String REMINDER_INSTANT_MESSAGE = "email-reminder";


  private String[] mServiceIDs;

  public ReminderConfiguration(String[] ids) {
    mServiceIDs = ids;
  }
  
  public ReminderConfiguration() {
      mServiceIDs = new String[]{ REMINDER_DEFAULT };
  }

  public ReminderConfiguration(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.readInt();

    int cnt = in.readInt();
    mServiceIDs = new String[cnt];
    for (int i=0; i<cnt; i++) {
      mServiceIDs[i] = (String)in.readObject();
    }

  }

  public void store(ObjectOutputStream out) throws IOException {
    out.writeInt(1);

    out.writeInt(mServiceIDs.length);
    for (String mServiceID : mServiceIDs) {
      out.writeObject(mServiceID);
    }
  }

  public String[] getReminderServices() {
    return mServiceIDs;
  }

  public void setReminderServices(String[] ids) {
    mServiceIDs = ids;
  }

  public boolean containsService(String service) {
    for (String mServiceID : mServiceIDs) {
      if (service.equals(mServiceID)) {
        return true;
      }
    }
    return false;
  }

}
