/*
 * $Id: TextArea.java 5937 2006-05-29 23:26:37Z eelco12 $ $Revision: 5937 $
 * $Date: 2006-05-30 01:26:37 +0200 (Tue, 30 May 2006) $
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
package wicket.markup.html.form;

import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.model.IModel;

/**
 * Multi-row text editing component.
 * 
 * @param <T>
 *            The type
 * 
 * @author Jonathan Locke
 */
public class TextArea<T> extends AbstractTextComponent<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see wicket.Component#Component(MarkupContainer,String)
	 */
	public TextArea(MarkupContainer parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * @see wicket.Component#Component(MarkupContainer,String, IModel)
	 */
	public TextArea(MarkupContainer parent, final String id, final IModel<T> model)
	{
		super(parent, id, model);
	}

	/**
	 * Handle the container's body.
	 * 
	 * @param markupStream
	 *            The markup stream
	 * @param openTag
	 *            The open tag for the body
	 * @see wicket.Component#onComponentTagBody(MarkupStream, ComponentTag)
	 */
	@Override
	protected final void onComponentTagBody(final MarkupStream markupStream,
			final ComponentTag openTag)
	{
		replaceComponentTagBody(markupStream, openTag, getValue());
	}
}