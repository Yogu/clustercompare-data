/*
 * $Id: IInitializer.java 4597 2006-02-21 22:08:11 +0000 (Tue, 21 Feb 2006)
 * jdonnerstag $ $Revision: 6058 $ $Date: 2006-02-21 22:08:11 +0000 (Tue, 21 Feb
 * 2006) $
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
package wicket;

import wicket.markup.html.PackageResource;

/**
 * Initializes something when application loads.
 * 
 * Initializer is there for clustering. Lets say you access a page that has a
 * link to a resource on node A now the url for the resource gets forwarded to
 * node B, but node B doesnt have the resource registered yet because maybe the
 * page class hasn't been loaded and so its static block hasn't run yet. So the
 * initializer is a place for you to register all those resources and do all the
 * stuff you used to do in the static blocks.
 * <p>
 * You don't have to pre-register {@link PackageResource package resources}, as
 * they can be initialized lazily.
 * </p>
 * 
 * @author Jonathan Locke
 */
public interface IInitializer
{
	/**
	 * @param application
	 *            The application loading the component
	 */
	void init(Application application);
}
