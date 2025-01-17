/*
 * $Id: Change.java 5883 2006-05-26 10:12:48Z joco01 $ $Revision: 5883 $ $Date:
 * 2005-05-18 14:27:45 +0200 (wo, 18 mei 2005) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.version.undo;

import java.io.Serializable;

/**
 * Undo information for a change to the page.
 * 
 * @author Jonathan Locke
 */
public abstract class Change implements Serializable
{
	/**
	 * Undoes the given change.
	 */
	public abstract void undo();
}
