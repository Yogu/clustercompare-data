/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar
 * 2006) eelco12 $ $Revision: 5004 $ $Date: 2006-03-17 20:47:08 -0800 (Fri, 17
 * Mar 2006) $
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
package wicket.markup.html;


/**
 * Guards lazily loaded {@link PackageResource package resources} against
 * unwanted access.
 * 
 * @author Eelco Hillenius
 */
public interface IPackageResourceGuard
{
	/**
	 * Whether the package resource that can be reached using the provided
	 * parameters may be accessed.
	 * 
	 * @param scope
	 *            This argument will be used to get the class loader for loading
	 *            the package resource, and to determine what package it is in
	 * @param path
	 *            The path to the resource
	 * 
	 * @param absolutePath
	 * @return True if access is permitted, false otherwise
	 */
	boolean accept(final Class scope, final String path);
}
