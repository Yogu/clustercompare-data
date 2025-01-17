/*
 * $Id: Link.java 6939 2006-08-11 19:26:43Z eelco12 $
 * $Revision: 6939 $ $Date: 2006-08-11 21:26:43 +0200 (Fri, 11 Aug 2006) $
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

import wicket.Application;
import wicket.Component;
import wicket.MarkupContainer;
import wicket.Page;
import wicket.RequestCycle;
import wicket.WicketRuntimeException;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.IModel;
import wicket.util.string.Strings;
import wicket.version.undo.Change;

/**
 * Implementation of a hyperlink component. A link can be used with an anchor
 * (&lt;a href...) element or any element that supports the onclick javascript
 * event handler (such as buttons, td elements, etc). When used with an anchor,
 * a href attribute will be generated. When used with any other element, an
 * onclick javascript event handler attribute will be generated.
 * <p>
 * You can use a link like:
 * 
 * <pre>
 *                          add(new Link(&quot;myLink&quot;)
 *                          {
 *                              public void onClick(RequestCycle cycle)
 *                              {
 *                                  // do something here...  
 *                              }
 *                          );
 * </pre>
 * 
 * and in your HTML file:
 * 
 * <pre>
 *                          &lt;a href=&quot;#&quot; wicket:id=&quot;myLink&quot;&gt;click here&lt;/a&gt;
 * </pre>
 * 
 * or:
 * 
 * <pre>
 *                          &lt;td wicket:id=&quot;myLink&quot;&gt;my clickable column&lt;/td&gt;
 * </pre>
 * 
 * </p>
 * The following snippet shows how to pass a parameter from the Page creating
 * the Page to the Page responded by the Link.
 * 
 * <pre>
 *                          add(new Link(&quot;link&quot;, listItem.getModel()) 
 *                          {
 *                              public void onClick() 
 *                              {
 *                                  MyObject obj = (MyObject)getModelObject();
 *                                  setResponsePage(new MyPage(obj.getId(), ... ));
 *                              }
 * </pre>
 * 
 * @param <T>
 *            Type of model object this component holds
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 */
public abstract class Link<T> extends WebMarkupContainer<T> implements ILinkListener
{
	/** Change record for when an anchor is changed. */
	private final class AnchorChange extends Change
	{
		private static final long serialVersionUID = 1L;

		/** the old anchor. */
		private Component anchor;

		/**
		 * Construct.
		 * 
		 * @param anchor
		 */
		public AnchorChange(Component anchor)
		{
			this.anchor = anchor;
		}

		@Override
		public final void undo()
		{
			Link.this.anchor = anchor;
		}
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Simple insertion string to allow disabled links to look like <i>Disabled
	 * link </i>.
	 */
	private String afterDisabledLink;

	/**
	 * An anchor (form 'http://server/app/etc#someAnchor') will be appended to
	 * the link so that after this link executes, it will jump to the provided
	 * anchor component's position. The provided anchor must either have the
	 * {@link Component#getOutputMarkupId()} flag true, or it must be attached
	 * to a &lt;a tag with a href attribute of more than one character starting
	 * with '#' ('&lt;a href="#someAnchor" ... ').
	 */
	private Component anchor;

	/**
	 * True if link should automatically enable/disable based on current page;
	 * false by default.
	 */
	private boolean autoEnable = false;

	/**
	 * Simple insertion string to allow disabled links to look like <i>Disabled
	 * link </i>.
	 */
	private String beforeDisabledLink;

	/**
	 * The popup specification. If not-null, a javascript on-click event handler
	 * will be generated that opens a new window using the popup properties.
	 */
	private PopupSettings popupSettings = null;

	/**
	 * @see wicket.Component#Component(MarkupContainer,String)
	 */
	public Link(MarkupContainer parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * @see wicket.Component#Component(MarkupContainer,String, IModel)
	 */
	public Link(MarkupContainer parent, final String id, IModel<T> object)
	{
		super(parent, id, object);
	}

	/**
	 * Gets the insertion string to allow disabled links to look like
	 * <i>Disabled link </i>.
	 * 
	 * @return The insertion string
	 */
	public String getAfterDisabledLink()
	{
		return afterDisabledLink;
	}

	/**
	 * Gets any anchor component.
	 * 
	 * @return Any anchor component to jump to, might be null
	 */
	public Component getAnchor()
	{
		return anchor;
	}

	/**
	 * Gets whether link should automatically enable/disable based on current
	 * page.
	 * 
	 * @return Whether this link should automatically enable/disable based on
	 *         current page.
	 */
	public final boolean getAutoEnable()
	{
		return autoEnable;
	}

	/**
	 * Gets the insertion string to allow disabled links to look like
	 * <i>Disabled link </i>.
	 * 
	 * @return The insertion string
	 */
	public String getBeforeDisabledLink()
	{
		return beforeDisabledLink;
	}

	/**
	 * Gets the popup specification. If not-null, a javascript on-click event
	 * handler will be generated that opens a new window using the popup
	 * properties.
	 * 
	 * @return the popup specification.
	 */
	public PopupSettings getPopupSettings()
	{
		return popupSettings;
	}

	/**
	 * @see wicket.Component#isEnabled()
	 */
	@Override
	public boolean isEnabled()
	{
		// If we're auto-enabling
		if (getAutoEnable())
		{
			// the link is enabled if this link doesn't link to the current page
			return !linksTo(getPage());
		}
		return super.isEnabled();
	}
	
	@Override
	protected boolean getStatelessHint()
	{
		return false;
	}

	/**
	 * Called when a link is clicked.
	 */
	public abstract void onClick();

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET API. DO NOT ATTEMPT TO OVERRIDE OR
	 * CALL IT.
	 * 
	 * Called when a link is clicked. The implementation of this method is
	 * currently to simply call onClick(), but this may be augmented in the
	 * future.
	 * 
	 * @see ILinkListener
	 */
	public final void onLinkClicked()
	{
		// if there are popupsettings and this link is clicked.
		// set the popup page map in the request parameters, so that pages that
		// are created in the onClick are made in the wanted pagemap
		if (popupSettings != null)
		{
			RequestCycle.get().getRequest().getRequestParameters().setPageMapName(
					popupSettings.getPageMap(this).getName());
		}
		// Invoke subclass handler
		onClick();
	}

	/**
	 * Sets the insertion string to allow disabled links to look like
	 * <i>Disabled link </i>.
	 * 
	 * @param afterDisabledLink
	 *            The insertion string
	 */
	public void setAfterDisabledLink(final String afterDisabledLink)
	{
		if (afterDisabledLink == null)
		{
			throw new IllegalArgumentException(
					"Value cannot be null.  For no text, specify an empty String instead.");
		}
		this.afterDisabledLink = afterDisabledLink;
	}

	/**
	 * Sets an anchor component. An anchor (form
	 * 'http://server/app/etc#someAnchor') will be appended to the link so that
	 * after this link executes, it will jump to the provided anchor component's
	 * position. The provided anchor must either have the
	 * {@link Component#getOutputMarkupId()} flag true, or it must be attached
	 * to a &lt;a tag with a href attribute of more than one character starting
	 * with '#' ('&lt;a href="#someAnchor" ... ').
	 * 
	 * @param anchor
	 *            The anchor
	 */
	public void setAnchor(Component anchor)
	{
		addStateChange(new AnchorChange(this.anchor));
		this.anchor = anchor;
	}

	/**
	 * Sets whether this link should automatically enable/disable based on
	 * current page.
	 * 
	 * @param autoEnable
	 *            whether this link should automatically enable/disable based on
	 *            current page.
	 * @return This
	 */
	public final Link setAutoEnable(final boolean autoEnable)
	{
		this.autoEnable = autoEnable;
		return this;
	}

	/**
	 * Sets the insertion string to allow disabled links to look like
	 * <i>Disabled link </i>.
	 * 
	 * @param beforeDisabledLink
	 *            The insertion string
	 */
	public void setBeforeDisabledLink(final String beforeDisabledLink)
	{
		if (beforeDisabledLink == null)
		{
			throw new IllegalArgumentException(
					"Value cannot be null.  For no text, specify an empty String instead.");
		}
		this.beforeDisabledLink = beforeDisabledLink;
	}

	/**
	 * Sets the popup specification. If not-null, a javascript on-click event
	 * handler will be generated that opens a new window using the popup
	 * properties.
	 * 
	 * @param popupSettings
	 *            the popup specification.
	 * @return This
	 */
	public final Link setPopupSettings(final PopupSettings popupSettings)
	{
		this.popupSettings = popupSettings;
		return this;
	}

	/**
	 * Appends any anchor to the url if the url is not null and the url does not
	 * already contain an anchor (url.indexOf('#') != -1). This implementation
	 * looks whether an anchor component was set, and if so, it will append the
	 * markup id of that component. That markup id is gotten by either calling
	 * {@link Component#getMarkupId()} if {@link Component#getOutputMarkupId()}
	 * returns true, or if the anchor component does not output it's id, this
	 * method will try to retrieve the id from the markup directly. If neither
	 * is found, an {@link WicketRuntimeException excpeption} is thrown. If no
	 * anchor component was set, but the link component is attached to a &lt;a
	 * element, this method will append what is in the href attribute <i>if</i>
	 * there is one, starts with a '#' and has more than one character.
	 * <p>
	 * You can override this method, but it means that you have to take care of
	 * whatever is done with any set anchor component yourself. You also have to
	 * manually append the '#' at the right place.
	 * </p>
	 * 
	 * @param tag
	 *            The component tag
	 * @param url
	 *            The url to start with
	 * @return The url, possibly with an anchor appended
	 */
	protected CharSequence appendAnchor(final ComponentTag tag, CharSequence url)
	{
		if (url != null)
		{
			Component anchor = getAnchor();
			if (anchor != null)
			{
				if (url.toString().indexOf('#') == -1)
				{
					String id;
					if (anchor.getOutputMarkupId())
					{
						id = anchor.getMarkupId();
					}
					else
					{
						id = anchor.getMarkupAttributes().getString("id");
					}

					if (id != null)
					{
						url = url + "#" + anchor.getMarkupId();
					}
					else
					{
						throw new WicketRuntimeException("an achor component was set on " + this
								+ " but it neither has outputMarkupId set to true "
								+ "nor has a id set explicitly");
					}
				}
			}
			else
			{
				if (tag.getName().equalsIgnoreCase("a"))
				{
					if (url.toString().indexOf('#') == -1)
					{
						String href = tag.getAttributes().getString("href");
						if (href != null && href.length() > 1 && href.charAt(0) == '#')
						{
							url = url + href;
						}
					}
				}
			}
		}
		return url;
	}

	/**
	 * @param url
	 *            The url for the link
	 * @return Any onClick JavaScript that should be used
	 */
	protected CharSequence getOnClickScript(final CharSequence url)
	{
		return null;
	}

	/**
	 * Gets the url to use for this link.
	 * 
	 * @return The URL that this link links to
	 */
	protected CharSequence getURL()
	{
		return urlFor(ILinkListener.INTERFACE);
	}

	/**
	 * @see wicket.Component#internalOnAttach()
	 */
	@Override
	protected void internalOnAttach()
	{
		// Set default for before/after link text
		if (beforeDisabledLink == null)
		{
			final Application app = getApplication();
			beforeDisabledLink = app.getMarkupSettings().getDefaultBeforeDisabledLink();
			afterDisabledLink = app.getMarkupSettings().getDefaultAfterDisabledLink();
		}
	}


	/**
	 * Whether this link refers to the given page.
	 * 
	 * @param page
	 *            A page
	 * @return True if this link goes to the given page
	 */
	protected boolean linksTo(final Page page)
	{
		return false;
	}

	/**
	 * Handles this link's tag.
	 * 
	 * @param tag
	 *            the component tag
	 * @see wicket.Component#onComponentTag(ComponentTag)
	 */
	@Override
	protected final void onComponentTag(final ComponentTag tag)
	{
		// Default handling for tag
		super.onComponentTag(tag);

		// Set href to link to this link's linkClicked method
		CharSequence url = getURL();

		// append any anchor
		url = appendAnchor(tag, url);

		// If we're disabled
		if (!isEnabled())
		{
			// if the tag is an anchor proper
			if (tag.getName().equalsIgnoreCase("a") || tag.getName().equalsIgnoreCase("link")
					|| tag.getName().equalsIgnoreCase("area"))
			{
				// Change anchor link to span tag
				tag.setName("span");

				// Remove any href from the old link
				tag.remove("href");

				// if it generates a popupscript, remove the design time JS
				// handler
				if (popupSettings != null)
				{
					tag.remove("onclick");
				}
			}
			else
			{
				// Remove any onclick design time code
				tag.remove("onclick");
			}
		}
		else
		{
			// if the tag is an anchor proper
			if (tag.getName().equalsIgnoreCase("a") || tag.getName().equalsIgnoreCase("link")
					|| tag.getName().equalsIgnoreCase("area"))
			{
				// generate the href attribute
				tag.put("href", Strings.replaceAll(url, "&", "&amp;"));

				// Add any popup script
				if (popupSettings != null)
				{
					// NOTE: don't encode to HTML as that is not valid
					// JavaScript
					tag.put("onclick", popupSettings.getPopupJavaScript());
				}
			}
			else
			{
				// generate a popup script by asking popup settings for one
				if (popupSettings != null)
				{
					popupSettings.setTarget("'" + url + "'");
					String popupScript = popupSettings.getPopupJavaScript();
					tag.put("onclick", popupScript);
				}
				else
				{
					// or generate an onclick JS handler directly
					tag.put("onclick", "location.href='" + url + "';");
				}
			}
		}

		// If the subclass specified javascript, use that
		final CharSequence onClickJavaScript = getOnClickScript(url);
		if (onClickJavaScript != null)
		{
			tag.put("onclick", onClickJavaScript);
		}
	}

	/**
	 * Renders this link's body.
	 * 
	 * @param markupStream
	 *            the markup stream
	 * @param openTag
	 *            the open part of this tag
	 * @see wicket.Component#onComponentTagBody(MarkupStream, ComponentTag)
	 */
	@Override
	protected final void onComponentTagBody(final MarkupStream markupStream,
			final ComponentTag openTag)
	{
		// Draw anything before the body?
		if (!isEnabled() && getBeforeDisabledLink() != null)
		{
			getResponse().write(getBeforeDisabledLink());
		}

		// Render the body of the link
		renderComponentTagBody(markupStream, openTag);

		// Draw anything after the body?
		if (!isEnabled() && getAfterDisabledLink() != null)
		{
			getResponse().write(getAfterDisabledLink());
		}
	}
}
