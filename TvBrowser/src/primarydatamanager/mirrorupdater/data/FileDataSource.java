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
 *     $Date: 2010-01-20 23:18:48 +0100 (Wed, 20 Jan 2010) $
 *   $Author: bananeweizen $
 * $Revision: 6380 $
 */
package primarydatamanager.mirrorupdater.data;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.logging.Logger;

import primarydatamanager.mirrorupdater.UpdateException;
import util.io.IOUtilities;

/**
 * 
 * 
 * @author Til Schneider, www.murfman.de
 */
public class FileDataSource implements DataSource {

  private static final Logger mLog
    = Logger.getLogger(FileDataSource.class.getName());

  private File mDir;

  private int mBytesRead;
  private int mFilesChecked;
  
  

  /**
   * @param dir
   */
  public FileDataSource(File dir) {
    mDir = dir;
  }



  public boolean fileExists(String fileName) throws UpdateException {
    mFilesChecked++;

    File file = new File(mDir, fileName);
    
    return file.exists();
  }



  public byte[] loadFile(String fileName) throws UpdateException {
    File file = new File(mDir, fileName);
    
    BufferedInputStream in = null;
    try {
      in = new BufferedInputStream(new FileInputStream(file), 0x4000);
      ByteArrayOutputStream out = new ByteArrayOutputStream((int) file.length());
      IOUtilities.pipeStreams(in, out);
      out.close();

      byte[] data = out.toByteArray();

      mBytesRead += data.length;
      
      return data;
    }
    catch (IOException exc) {
      throw new UpdateException("Loading file failed: " + fileName, exc);
    }
    finally {
      if (in != null) {
        try { in.close(); } catch (IOException exc) {}
      }
    }
  }


  public void close() throws UpdateException {
    mLog.info("In total there were "
      + NumberFormat.getInstance().format(mFilesChecked) + " files checked and "
      + NumberFormat.getInstance().format(mBytesRead) + " bytes read.");
  }

}
