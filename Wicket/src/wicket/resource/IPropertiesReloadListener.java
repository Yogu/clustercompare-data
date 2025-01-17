/*
 * $Id: ComponentStringResourceLoader.java,v 1.5 2005/01/19 08:07:57
 * jonathanlocke Exp $ $Revision: 5873 $ $Date: 2006-01-02 07:37:31 +0000 (Mon,
 * 02 Jan 2006) $
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
package wicket.resource;

/**
 * To be implemented by listeners interested in PropertiesFactory events fired
 * after a new properties files has been loaded
 * 
 * @author Juergen Donnerstag
 */
public abstract interface IPropertiesReloadListener
{
	/**
	 * Fired after a new properties files has been loaded
	 * 
	 * @param key
	 */
	void propertiesLoaded(final String key);
}