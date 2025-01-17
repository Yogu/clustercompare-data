/*
 * $Id: MapVariableInterpolator.java 5791 2006-05-20 00:32:57 +0000 (Sat, 20 May
 * 2006) joco01 $ $Revision: 5874 $ $Date: 2006-05-20 00:32:57 +0000 (Sat, 20
 * May 2006) $
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

import java.util.Map;

/**
 * Interpolates variables into a string from a Map.
 * 
 * @author Jonathan Locke
 */
public class MapVariableInterpolator extends VariableInterpolator
{
	/** Map of variables */
	private Map variables;

	/**
	 * Constructor
	 * 
	 * @param string
	 *            The string to interpolate into
	 * @param variables
	 *            The variables to substitute
	 */
	public MapVariableInterpolator(final String string, final Map variables)
	{
		super(string);
		this.variables = variables;
	}

	/**
	 * Accessor to set variables
	 * 
	 * @param variables
	 *            New value
	 */
	public final void setVariables(final Map variables)
	{
		this.variables = variables;
	}

	/**
	 * Gets a value for a variable name during interpolation
	 * 
	 * @param variableName
	 *            The variable
	 * @return The value
	 */
	@Override
	protected final String getValue(final String variableName)
	{
		return variables.get(variableName).toString();
	}

	/**
	 * Interpolate the string with the arguments defined in a map
	 * 
	 * @param string
	 * @param variables
	 * @return interpolated string
	 */
	public static String interpolate(String string, Map variables)
	{
		return new MapVariableInterpolator(string, variables).toString();
	}

}
