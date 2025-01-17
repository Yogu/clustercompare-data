/*
 * $Id: SystemVariableInterpolator.java,v 1.4 2005/01/15 19:24:02 jonathanlocke
 * Exp $ $Revision: 1115 $ $Date: 2005-02-22 18:48:25 +0100 (Tue, 22 Feb 2005) $
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
package wicket.util.string.interpolator;

/**
 * Convenient way of interpolating system properties.
 * 
 * @author Jonathan Locke
 */
public final class SystemVariableInterpolator extends MapVariableInterpolator
{
	/**
	 * Constructor
	 * 
	 * @param string
	 *            The string to interpolate with system properties
	 */
	public SystemVariableInterpolator(final String string)
	{
		super(string, System.getProperties());
	}
}
