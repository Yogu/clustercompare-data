/*
 * $Id: BoxBorder.java 5883 2006-05-26 10:12:48Z joco01 $ $Revision: 5883 $
 * $Date: 2006-05-26 12:12:48 +0200 (Fri, 26 May 2006) $
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
package wicket.markup.html.border;

import wicket.MarkupContainer;

/**
 * A very simple border component that draws a thin black line around its
 * children.
 * 
 * @author Jonathan Locke
 */
public final class BoxBorder extends Border
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see wicket.Component#Component(MarkupContainer,String)
	 */
	public BoxBorder(MarkupContainer parent, final String id)
	{
		super(parent, id);
	}
}
