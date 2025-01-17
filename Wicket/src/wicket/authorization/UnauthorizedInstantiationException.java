/*
 * $Id: UnauthorizedInstantiationException.java,v 1.2 2006/01/02 07:15:49
 * jonathanlocke Exp $ $Revision: 5869 $ $Date: 2006-02-04 02:34:59 +0000 (Sat,
 * 04 Feb 2006) $
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

/**
 * Exception that is thrown when the creation of a component is not allowed.
 * 
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public class UnauthorizedInstantiationException extends AuthorizationException
{
	private static final long serialVersionUID = 1L;

	/** The component class that could not be instantiated */
	private Class componentClass;

	/**
	 * Construct.
	 * 
	 * @param componentClass
	 *            The unauthorized component class
	 */
	public UnauthorizedInstantiationException(final Class componentClass)
	{
		super("Not authorized to instantiate class " + componentClass.getName());
		this.componentClass = componentClass;
	}

	/**
	 * @return The component class that could not be instantiated
	 */
	public Class getComponentClass()
	{
		return componentClass;
	}
}
