/*
 * $Id: ComponentInstantiationAuthorizer.java,v 1.4 2006/02/11 07:31:12 eelco12
 * Exp $ $Revision: 4301 $ $Date: 2006-02-11 11:22:03 +0100 (Sat, 11 Feb 2006) $
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
package wicket.authorization;

import wicket.Component;

/**
 * Interface to code that is called to handle unauthorized component
 * instantiations.
 * 
 * @author Jonathan Locke
 */
public interface IUnauthorizedComponentInstantiationListener
{
	/**
	 * Called when an unauthorized component instantiation is about to take
	 * place (but before it happens).
	 * 
	 * @param component
	 *            The partially constructed component (only the id is guaranteed
	 *            to be valid).
	 * @see wicket.application.IComponentInstantiationListener
	 */
	void onUnauthorizedInstantiation(Component component);
}
