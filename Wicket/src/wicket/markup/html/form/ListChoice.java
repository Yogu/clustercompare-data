/*
 * $Id: ListChoice.java 6658 2006-07-24 18:44:51Z jannehietamaki $ $Revision: 6658 $
 * $Date: 2006-07-24 20:44:51 +0200 (Mon, 24 Jul 2006) $
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

import java.util.List;

import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.model.IModel;

/**
 * Essentially a drop down choice that doesn't drop down. Instead, it scrolls
 * and displays a given number of rows.
 * 
 * @param <T>
 * 			The type of the model object.
 *  
 * @author Jonathan Locke
 * @author Johan Compagner
 * @author Eelco Hillenius
 */
public class ListChoice<T> extends DropDownChoice<T>
{
	private static final long serialVersionUID = 1L;

	/** The default maximum number of rows to display. */
	private static int defaultMaxRows = 8;

	/** The maximum number of rows to display. */
	private int maxRows;

	/**
	 * Gets the default maximum number of rows to display.
	 * 
	 * @return Returns the defaultMaxRows.
	 */
	protected static int getDefaultMaxRows()
	{
		return defaultMaxRows;
	}

	/**
	 * Sets the default maximum number of rows to display.
	 * 
	 * @param defaultMaxRows
	 *            The defaultMaxRows to set.
	 */
	@SuppressWarnings("unchecked")
	protected static void setDefaultMaxRows(final int defaultMaxRows)
	{
		ListChoice.defaultMaxRows = defaultMaxRows;
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String)
	 */
	public ListChoice(MarkupContainer parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      List)
	 */
	public ListChoice(MarkupContainer parent, final String id, final List<T> values)
	{
		super(parent, id, values);
	}

	/**
	 * @param id
	 *            See Component
	 * @param choices
	 *            The collection of values in the list
	 * @param renderer
	 *            See AbstractChoice
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      List,IChoiceRenderer)
	 */
	public ListChoice(MarkupContainer parent, final String id, final List<T> choices,
			final IChoiceRenderer<T> renderer)
	{
		super(parent, id, choices, renderer);
	}

	/**
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of values in the list
	 * @param renderer
	 *            See AbstractChoice
	 * @see DropDownChoice#DropDownChoice(MarkupContainer,String, IModel, List)
	 */
	public ListChoice(MarkupContainer parent, final String id, final IModel<T> model,
			final List<T> choices, final IChoiceRenderer<T> renderer)
	{
		this(parent, id, model, choices, renderer, defaultMaxRows);
	}

	/**
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of values in the list
	 * @param renderer
	 *            See AbstractChoice
	 * @param maxRows
	 *            Maximum number of rows to show
	 * @see DropDownChoice#DropDownChoice(MarkupContainer,String, IModel, List)
	 */
	public ListChoice(MarkupContainer parent, final String id, final IModel<T> model,
			final List<T> choices, final IChoiceRenderer<T> renderer, final int maxRows)
	{
		super(parent, id, model, choices, renderer);
		this.maxRows = maxRows;
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      IModel)
	 */
	public ListChoice(MarkupContainer parent, String id, IModel<List<T>> choices)
	{
		super(parent, id, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      IModel,IModel)
	 */
	public ListChoice(MarkupContainer parent, String id, IModel<T>model, IModel<List<T>> choices)
	{
		super(parent, id, model, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      IModel,IChoiceRenderer)
	 */
	public ListChoice(MarkupContainer parent, String id, IModel<List<T>> choices, IChoiceRenderer<T> renderer)
	{
		super(parent, id, choices, renderer);
	}


	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      IModel, IModel,IChoiceRenderer)
	 */
	public ListChoice(MarkupContainer parent, String id, IModel<T> model, IModel<List<T>> choices,
			IChoiceRenderer<T> renderer)
	{
		super(parent, id, model, choices, renderer);
	}

	/**
	 * Gets the maximum number of rows to display.
	 * 
	 * @return the maximum number of rows to display
	 */
	public final int getMaxRows()
	{
		return maxRows;
	}

	/**
	 * Sets the maximum number of rows to display.
	 * 
	 * @param maxRows
	 *            the maximum number of rows to display
	 * @return This
	 */
	public final ListChoice setMaxRows(int maxRows)
	{
		this.maxRows = maxRows;
		return this;
	}

	/**
	 * @see wicket.Component#onComponentTag(ComponentTag)
	 */
	@Override
	protected final void onComponentTag(final ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.put("size", maxRows);
	}

	/**
	 * @see wicket.markup.html.form.FormComponent#supportsPersistence()
	 */
	@Override
	protected final boolean supportsPersistence()
	{
		return true;
	}

	/**
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of values in the list
	 * @see DropDownChoice#DropDownChoice(MarkupContainer,String, IModel, List)
	 */
	public ListChoice(MarkupContainer parent, final String id, final IModel<T> model,
			final List<T> choices)
	{
		this(parent, id, model, choices, defaultMaxRows);
	}

	/**
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of values in the list
	 * @param maxRows
	 *            Maximum number of rows to show
	 * @see DropDownChoice#DropDownChoice(MarkupContainer,String, IModel, List)
	 */
	public ListChoice(MarkupContainer parent, final String id, final IModel<T> model,
			final List<T> choices, final int maxRows)
	{
		super(parent, id, model, choices);
		this.maxRows = maxRows;
	}
}
