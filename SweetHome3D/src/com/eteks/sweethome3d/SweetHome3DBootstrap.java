/*
 * SweetHome3DBootstrap.java 2 sept. 07
 *
 * Copyright (c) 2007 Emmanuel PUYBARET / eTeks <info@eteks.com>. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.eteks.sweethome3d;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.eteks.sweethome3d.tools.ExtensionsClassLoader;

/**
 * This bootstrap class loads Sweet Home 3D application classes from jars in classpath 
 * or from extension jars stored as resources.
 * @author Emmanuel Puybaret
 */
public class SweetHome3DBootstrap {
  public static void main(String [] args) throws MalformedURLException, IllegalAccessException, 
        InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
    Class sweetHome3DBootstrapClass = SweetHome3DBootstrap.class;
    List<String> extensionJarsAndDlls = new ArrayList<String>(Arrays.asList(new String [] {
        "iText-2.1.5.jar", // Jars included in Sweet Home 3D executable jar file 
        "freehep-vectorgraphics-svg-2.1.1.jar",
        "Loader3DS1_2u.jar",
        "sunflow-0.07.3f.jar",
        "jmf.jar",
        "jnlp.jar",
        "j3dcore.jar", // Main Java 3D jars
        "vecmath.jar",
        "j3dutils.jar",
        "macosx/gluegen-rt.jar", // Mac OS X jars and DLLs
        "macosx/jogl.jar",
        "macosx/libgluegen-rt.jnilib",
        "macosx/libjogl.jnilib",
        "macosx/libjogl_awt.jnilib",
        "macosx/libjogl_cg.jnilib"}));
    if ("64".equals(System.getProperty("sun.arch.data.model"))) {
      extensionJarsAndDlls.add("linux/x64/libj3dcore-ogl.so"); // Linux 64 bits DLLs
      extensionJarsAndDlls.add("windows/x64/j3dcore-ogl.dll"); // Windows 64 bits DLLs
    } else {
      extensionJarsAndDlls.add("linux/i386/libj3dcore-ogl.so"); // Linux 32 bits DLLs
      extensionJarsAndDlls.add("linux/i386/libj3dcore-ogl-cg.so"); // Windows 32 bits DLLs
      extensionJarsAndDlls.add("windows/i386/j3dcore-d3d.dll");
      extensionJarsAndDlls.add("windows/i386/j3dcore-ogl.dll");
      extensionJarsAndDlls.add("windows/i386/j3dcore-ogl-cg.dll");
      extensionJarsAndDlls.add("windows/i386/j3dcore-ogl-chk.dll");
    }
    
    String [] applicationPackages = {
        "com.eteks.sweethome3d",
        "javax.media",
        "javax.vecmath",
        "com.sun.j3d",
        "com.sun.opengl",
        "com.sun.gluegen.runtime",
        "javax.media.opengl",
        "com.sun.media",
        "com.ibm.media",
        "jmpapps.util",
        "com.microcrowd.loader.java3d",
        "org.sunflow"};
    ClassLoader java3DClassLoader = new ExtensionsClassLoader(
        sweetHome3DBootstrapClass.getClassLoader(), 
        sweetHome3DBootstrapClass.getProtectionDomain(),
        extensionJarsAndDlls.toArray(new String [extensionJarsAndDlls.size()]), applicationPackages);  
    
    String applicationClassName = "com.eteks.sweethome3d.SweetHome3D";
    Class applicationClass = java3DClassLoader.loadClass(applicationClassName);
    Method applicationClassMain = 
      applicationClass.getMethod("main", Array.newInstance(String.class, 0).getClass());
    // Call application class main method with reflection
    applicationClassMain.invoke(null, new Object [] {args});
  }
}