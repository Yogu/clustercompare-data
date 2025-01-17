/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.sf.jftp.net;

import net.sf.jftp.util.StringUtils;
import net.sf.jftp.util.Log;
import net.sf.jftp.config.Settings;

import java.lang.reflect.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * An URLStreamHandler for handling ftp urls.
 */
public class FtpURLStreamHandler extends URLStreamHandler
{
    public FtpURLStreamHandler()
    {
        super();
    }

    public URLConnection openConnection(URL u)
    {
        return null;
    }
}
