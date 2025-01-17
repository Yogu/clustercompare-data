/*
 * $Id: IAjaxLink.java 5125 2006-03-25 19:42:10 +0000 (Sat, 25 Mar 2006)
 * ivaynberg $ $Revision: 5868 $ $Date: 2006-03-25 19:42:10 +0000 (Sat, 25 Mar
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
package wicket.ajax.markup.html;

import wicket.ajax.AjaxRequestTarget;

/**
 * Interface for Ajax callback links.
 * 
 * @since 1.2
 * 
 * @author Igor Vaynberg (ivaynberg)
 * @author Martijn Dashorst
 */
public interface IAjaxLink
{
	/**
	 * Listener method invoked on the ajax request generated when the user
	 * clicks the link
	 * 
	 * @param target
	 *            the request target.
	 */
	void onClick(final AjaxRequestTarget target);
}
