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
 *     $Date: 2006-02-06 23:24:45 +0100 (Mon, 06 Feb 2006) $
 *   $Author: ds10 $
 * $Revision: 1815 $
 */

package util.ui;

import javax.swing.JRootPane;

/**
 * The Interface for easy closing Windows with Escape key.
 * 
 * @author Ren� Mach
 *
 */
public interface WindowClosingIf {
  /**
   * The closing method.
   *
   */
  public void close();
  
  /**
   * 
   * @return The RootPane to register the Escape key action with.
   */
  public JRootPane getRootPane();
}
