/*
 * $Id: ListenerSet.java 5771 2006-05-19 12:04:06 +0000 (Fri, 19 May 2006)
 * joco01 $ $Revision: 5873 $ $Date: 2006-05-19 12:04:06 +0000 (Fri, 19 May
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

import java.util.HashSet;
import java.util.Set;

/**
 * Holds a set of listeners implementing the IListener tag interface. Subclasses
 * can implement notification methods that cast the listener to the correct
 * subinterface and invoke the appropriate listener method.
 * 
 * @author Jonathan Locke
 */
public abstract class ListenerSet
{
	/** Set of change listeners */
	private final Set<IListener> listeners = new HashSet<IListener>();

	/**
	 * Adds a listener to this set of listeners.
	 * 
	 * @param listener
	 *            The listener to add
	 * @return <tt>true</tt> if the set did not already contain the specified
	 *         listener.
	 */
	public boolean add(final IListener listener)
	{
		return listeners.add(listener);
	}

	/**
	 * Notifies each listener in this set by calling notifyListener.
	 */
	public void notifyListeners()
	{
		// Create a stable copy for iterating over
		final Set<IListener> copy = new HashSet<IListener>(listeners);

		for (IListener listener : copy)
		{
			notifyListener(listener);
		}
	}

	/**
	 * Removes a listener from this set.
	 * 
	 * @param listener
	 *            The listener to remove
	 */
	public void remove(final IListener listener)
	{
		listeners.remove(listener);
	}

	/**
	 * Notifies a listener.
	 * 
	 * @param listener
	 *            The listener to notify
	 */
	protected abstract void notifyListener(IListener listener);
}
