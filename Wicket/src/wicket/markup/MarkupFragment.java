/*
 * $Id: MarkupFragment.java 5791 2006-05-20 00:32:57 +0000 (Sat, 20 May 2006)
 * joco01 $ $Revision: 6957 $ $Date: 2006-05-20 00:32:57 +0000 (Sat, 20 May
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
package wicket.markup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.WicketRuntimeException;
import wicket.util.string.AppendingStringBuffer;

/**
 * A list of markup elements associated with a Markup. Might be all elements of
 * a markup resource, might be just the elements associated with a specific tag.
 * 
 * @see wicket.markup.Markup
 * @see wicket.markup.MarkupElement
 * @see wicket.markup.ComponentTag
 * @see wicket.markup.RawMarkup
 * 
 * @author Juergen Donnerstag
 */
public class MarkupFragment extends MarkupElement implements Iterable<MarkupElement>
{
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(MarkupFragment.class);

	/** Placeholder that indicates no markup */
	public static final MarkupFragment NO_MARKUP_FRAGMENT = new MarkupFragment(IMarkup.NO_MARKUP);

	/** The list of markup elements */
	private/* final */List<MarkupElement> markupElements;

	/** The associate markup */
	private final IMarkup markup;

	/**
	 * If null, than markup fragment is associated with a whole Page or Panel
	 * markup file. Else the parent Wicket tag's markup
	 */
	private final MarkupFragment parentFragment;

	/**
	 * Constructor.
	 * <p>
	 * This constructor should be used for Pages and Panels which have there own
	 * associated markup file
	 * 
	 * @param markup
	 *            The associated Markup
	 */
	MarkupFragment(final IMarkup markup)
	{
		this(markup, null, null);
	}

	/**
	 * Constructor
	 * <p>
	 * This constructor should be used for tags inside a markup file.
	 * 
	 * @param markup
	 *            The associated Markup
	 * @param parentFragment
	 *            The parent Wicket tag's markup fragment
	 * @param openTag
	 *            The initial (open) tag
	 */
	MarkupFragment(final IMarkup markup, final MarkupFragment parentFragment,
			final ComponentTag openTag)
	{
		this.markup = markup;
		this.parentFragment = parentFragment;
		this.markupElements = new ArrayList<MarkupElement>();

		if (this.parentFragment != null)
		{
			this.parentFragment.addMarkupElement(this);
		}

		if (openTag != null)
		{
			this.markupElements.add(openTag);
		}
	}

	/**
	 * Get the parent markup fragment.
	 * 
	 * @return Null, if no parent available
	 */
	public final MarkupFragment getParentFragment()
	{
		return this.parentFragment;
	}

	/**
	 * For Wicket it would be sufficient for this method to be package
	 * protected. However to allow wicket-bench easy access to the information
	 * ...
	 * 
	 * @param index
	 *            Index into markup list
	 * @return Markup element
	 */
	public final MarkupElement get(final int index)
	{
		return markupElements.get(index);
	}

	/**
	 * Get the markup fragment associated with the id
	 * 
	 * @param id
	 *            The id of the child tag
	 * @return Markup fragment
	 */
	public final MarkupFragment getChildFragment(final String id)
	{
		if ((id == null) || (id.length() == 0))
		{
			return null;
		}

		for (MarkupElement elem : this)
		{
			if (elem instanceof MarkupFragment)
			{
				MarkupFragment fragment = (MarkupFragment)elem;
				ComponentTag tag = (ComponentTag)fragment.get(0);
				String tagId = tag.getId();
				if ((tagId != null) && tagId.equals(id))
				{
					return fragment;
				}
			}
		}

		return null;
	}

	/**
	 * Gets the associate markup
	 * 
	 * @return The associated markup
	 */
	public final IMarkup getMarkup()
	{
		return this.markup;
	}

	/**
	 * For Wicket it would be sufficient for this method to be package
	 * protected. However to allow wicket-bench easy access to the information
	 * ...
	 * 
	 * @return Number of markup elements
	 */
	public int size()
	{
		return markupElements.size();
	}

	/**
	 * Add a MarkupElement
	 * 
	 * @param markupElement
	 */
	final void addMarkupElement(final MarkupElement markupElement)
	{
		this.markupElements.add(markupElement);
	}

	/**
	 * Add a MarkupElement
	 * 
	 * @param pos
	 * @param markupElement
	 */
	final void addMarkupElement(final int pos, final MarkupElement markupElement)
	{
		this.markupElements.add(pos, markupElement);
	}

	/**
	 * Make all tags immutable and the list of elements unmodifable.
	 */
	final void makeImmutable()
	{
		for (MarkupElement elem : this)
		{
			if (elem instanceof ComponentTag)
			{
				// Make the tag immutable
				((ComponentTag)elem).makeImmutable();
			}
		}

		this.markupElements = Collections.unmodifiableList(this.markupElements);
	}

	/**
	 * Iterator for MarkupElements
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public final Iterator<MarkupElement> iterator()
	{
		return new Iterator<MarkupElement>()
		{
			private int index = 0;

			public boolean hasNext()
			{
				return (index < size());
			}

			public MarkupElement next()
			{
				return get(index++);
			}

			public void remove()
			{
				throw new WicketRuntimeException(
						"remomve() not supported by MarkupFragment Iterator");
			}
		};
	}

	/**
	 * @return String representation of markup list
	 */
	@Override
	public final String toString()
	{
		return this.markupElements.toString();
	}

	/**
	 * @see wicket.markup.MarkupElement#toCharSequence()
	 */
	@Override
	public CharSequence toCharSequence()
	{
		final AppendingStringBuffer buf = new AppendingStringBuffer(this.markup.size() * 40);
		for (MarkupElement elem : this)
		{
			buf.append(elem);
			buf.append(",");
		}
		return buf;
	}

	/**
	 * @see wicket.markup.MarkupElement#toUserDebugString()
	 */
	@Override
	public String toUserDebugString()
	{
		return toString();
	}
}
