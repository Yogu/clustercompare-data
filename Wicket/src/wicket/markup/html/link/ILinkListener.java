/*
 * $Id: ILinkListener.java 4381 2006-02-13 04:10:14 +0000 (Mon, 13 Feb 2006)
 * jonathanlocke $ $Revision: 5871 $ $Date: 2006-02-13 04:10:14 +0000 (Mon, 13
 * Feb 2006) $
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
package wicket.markup.html.link;

import wicket.IRequestListener;
import wicket.RequestListenerInterface;

/**
 * Listener method for link clicks.
 * 
 * @author Jonathan Locke
 */
public interface ILinkListener extends IRequestListener
{
	/** Listener interface */
	public static final RequestListenerInterface INTERFACE = new RequestListenerInterface(
			ILinkListener.class);

	/**
	 * Called when a link is clicked.
	 */
	void onLinkClicked();
}