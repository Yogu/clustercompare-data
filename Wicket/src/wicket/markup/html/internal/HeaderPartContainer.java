/*
 * $Id: Border.java 4831 2006-03-08 13:32:22 -0800 (Wed, 08 Mar 2006)
 * jdonnerstag $ $Revision: 6433 $ $Date: 2006-03-08 13:32:22 -0800 (Wed, 08 Mar
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
package wicket.markup.html.internal;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.resolver.IComponentResolver;

/**
 * For each wicket:head tag a HeaderPartContainer is created and added to the
 * HtmlHeaderContainer which has been added to the Page.
 * 
 * @author Juergen Donnerstag
 */
public final class HeaderPartContainer extends WebMarkupContainer implements IComponentResolver
{
	private static final long serialVersionUID = 1L;

	/** The panel or bordered page the header part is associated with */
	private final MarkupContainer container;

	/** <wicket:head scope="...">. A kind of namespace */
	private final String scope;

	/**
	 * @param parent
	 *            The parent of this component
	 * @param id
	 *            The component id
	 * @param container
	 *            The Panel (or bordered page) the header part is associated
	 *            with
	 * @param scope
	 *            The scope of the wicket:head tag
	 */
	public HeaderPartContainer(MarkupContainer parent, final String id,
			final MarkupContainer container, final String scope)
	{
		super(parent, id);
		this.container = container;
		this.scope = scope;
	}

	/**
	 * Get the scope of the header part
	 * 
	 * @return The scope name
	 */
	public final String getScope()
	{
		return this.scope;
	}

	/**
	 * @see IComponentResolver#resolve(MarkupContainer, MarkupStream,
	 *      ComponentTag)
	 */
	public final boolean resolve(final MarkupContainer container, final MarkupStream markupStream,
			final ComponentTag tag)
	{
		// The tag must be resolved against the panel and not against the
		// page
		Component component = this.container.get(tag.getId());
		if (component != null)
		{
			component.render(markupStream);
			return true;
		}

		return false;
	}

	/**
	 * @see #setMarkupStream(MarkupStream)
	 * 
	 * @param markupStream
	 */
	public final void setMyMarkupStream(final MarkupStream markupStream)
	{
		super.setMarkupStream(markupStream);
	}
}