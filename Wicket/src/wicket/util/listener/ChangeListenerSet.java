/*
 * $Id: ChangeListenerSet.java 5791 2006-05-20 00:32:57 +0000 (Sat, 20 May 2006)
 * joco01 $ $Revision: 5873 $ $Date: 2006-05-20 00:32:57 +0000 (Sat, 20 May
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
package wicket.util.listener;

/**
 * Holds a set of IChangeListeners.
 * 
 * @author Jonathan Locke
 */
public final class ChangeListenerSet extends ListenerSet
{
	/**
	 * @see wicket.util.listener.ListenerSet#notifyListener(wicket.util.listener.IListener)
	 */
	@Override
	protected void notifyListener(IListener listener)
	{
		((IChangeListener)listener).onChange();
	}
}
