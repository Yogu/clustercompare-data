/*
 * $Id: IComponentInstantiationListener.java 4233 2006-02-09 01:50:17 +0000
 * (Thu, 09 Feb 2006) eelco12 $ $Revision: 5869 $ $Date: 2006-02-09 01:50:17
 * +0000 (Thu, 09 Feb 2006) $
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
package wicket.application;

import wicket.Component;

/**
 * Listener interface that receives messages when components are constructed.
 * This mechanism is used by {@link wicket.authorization.IAuthorizationStrategy},
 * but can additionally be used to implement things like dependency injection
 * support etc.
 * 
 * <strong>Implementations must be thread safe</strong>
 * 
 * @author Eelco Hillenius
 */
public interface IComponentInstantiationListener
{
	/**
	 * Called for every component that is instantiated. This method is called
	 * <strong>during</strong> construction, so do not depend on the
	 * construction being completed yet. The id is guaranteed to be set before
	 * this call.
	 * 
	 * @param component
	 *            the component that is being instantiated.
	 */
	void onInstantiation(Component component);
}
