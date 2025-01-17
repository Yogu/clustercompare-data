/*
 * $Id: FormComponentFeedbackBorder.java,v 1.3 2005/01/02 21:58:21 jonathanlocke
 * Exp $ $Revision: 6017 $ $Date: 2006-06-03 09:45:51 +0200 (Sat, 03 Jun 2006) $
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
package wicket.markup.html.form.validation;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.feedback.ComponentFeedbackMessageFilter;
import wicket.feedback.IFeedback;
import wicket.feedback.IFeedbackMessageFilter;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;

/**
 * A panel that hides or shows itself depending on whether there are feedback
 * messages for a given message filter. If a component is set using
 * setIndicatorFor(Component), then the indicator is visible when the given
 * component has an error. The default content for this indicator is a red star,
 * but you can subclass this panel and provide your own markup to give any
 * custom look you desire.
 * 
 * @author Jonathan Locke
 * @param <T> 
 */
public class FormComponentFeedbackIndicator<T> extends Panel<T> implements IFeedback
{
	private static final long serialVersionUID = 1L;

	/** The message filter for this indicator component */
	private IFeedbackMessageFilter filter;

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            See Component
	 */
	public FormComponentFeedbackIndicator(MarkupContainer parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * @see wicket.Component#Component(MarkupContainer,String, IModel)
	 */
	public FormComponentFeedbackIndicator(MarkupContainer parent, final String id, IModel<T> model)
	{
		super(parent, id, model);
	}

	/**
	 * @param component
	 *            The component to watch for messages
	 */
	public void setIndicatorFor(final Component component)
	{
		filter = new ComponentFeedbackMessageFilter(component);
	}

	/**
	 * @see wicket.feedback.IFeedback#updateFeedback()
	 */
	public void updateFeedback()
	{
		// Get the messages for the current page
		setVisible(getPage().getFeedbackMessages().hasMessage(getFeedbackMessageFilter()));
	}

	/**
	 * @return Let subclass specify some other filter
	 */
	protected IFeedbackMessageFilter getFeedbackMessageFilter()
	{
		return filter;
	}
}
