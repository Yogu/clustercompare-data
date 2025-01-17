/*
 * $Id: EmptyRequestTarget.java 5791 2006-05-20 00:32:57 +0000 (Sat, 20 May
 * 2006) joco01 $ $Revision: 5872 $ $Date: 2006-05-20 00:32:57 +0000 (Sat, 20
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
package wicket.request.target.basic;

import wicket.IRequestTarget;
import wicket.RequestCycle;

/**
 * The empty request target does nothing in itself but instead relies on some
 * other source to generate a response. It can be regarded as a dummy
 * implementation.
 * 
 * @author Eelco Hillenius
 */
public final class EmptyRequestTarget implements IRequestTarget
{
	/** immutable hashcode. */
	private static final int HASH = 17 * 3214424;

	/** singleton instance. */
	private static final EmptyRequestTarget instance = new EmptyRequestTarget();

	/**
	 * Construct.
	 */
	private EmptyRequestTarget()
	{
	}

	/**
	 * Gets the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static final EmptyRequestTarget getInstance()
	{
		return instance;
	}

	/**
	 * Does nothing at all.
	 * 
	 * @see wicket.IRequestTarget#respond(wicket.RequestCycle)
	 */
	public void respond(RequestCycle requestCycle)
	{
	}

	/**
	 * @see wicket.IRequestTarget#detach(wicket.RequestCycle)
	 */
	public void detach(RequestCycle requestCycle)
	{
	}

	/**
	 * @see wicket.IRequestTarget#getLock(RequestCycle)
	 */
	public Object getLock(RequestCycle requestCycle)
	{
		return null;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof EmptyRequestTarget)
		{
			return true;
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return HASH;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "EmptyRequestTarget";
	}
}
