/*
 * $Id: BookmarkablePageRequestTargetUrlCodingStrategy.java,v 1.1 2005/12/10
 * 21:28:56 eelco12 Exp $ $Revision: 6864 $ $Date: 2006-05-24 20:53:56 +0000
 * (Wed, 24 May 2006) $
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
package wicket.request.target.coding;

import wicket.Component;
import wicket.IRequestTarget;
import wicket.Page;
import wicket.PageParameters;
import wicket.WicketRuntimeException;
import wicket.protocol.http.request.WebRequestCodingStrategy;
import wicket.request.RequestParameters;
import wicket.request.target.component.BookmarkableListenerInterfaceRequestTarget;
import wicket.request.target.component.BookmarkablePageRequestTarget;
import wicket.request.target.component.IBookmarkablePageRequestTarget;
import wicket.util.string.AppendingStringBuffer;
import wicket.util.string.Strings;

/**
 * Encodes and decodes mounts for a single bookmarkable page class.
 * 
 * @author Eelco Hillenius
 */
public class BookmarkablePageRequestTargetUrlCodingStrategy
		extends
			AbstractRequestTargetUrlCodingStrategy
{
	/** bookmarkable page class. */
	private final Class<? extends Page> bookmarkablePageClass;

	/** page map name. */
	private final String pageMapName;

	/**
	 * Construct.
	 * 
	 * @param mountPath
	 *            the mount path
	 * @param bookmarkablePageClass
	 *            the class of the bookmarkable page
	 * @param pageMapName
	 *            the page map name if any
	 */
	public BookmarkablePageRequestTargetUrlCodingStrategy(final String mountPath,
			final Class<? extends Page> bookmarkablePageClass, String pageMapName)
	{
		super(mountPath);

		if (bookmarkablePageClass == null)
		{
			throw new IllegalArgumentException("Argument bookmarkablePageClass must be not null");
		}

		this.bookmarkablePageClass = bookmarkablePageClass;
		this.pageMapName = pageMapName;
	}

	/**
	 * @see wicket.request.target.coding.IRequestTargetUrlCodingStrategy#encode(wicket.IRequestTarget)
	 */
	public final CharSequence encode(final IRequestTarget requestTarget)
	{
		if (!(requestTarget instanceof IBookmarkablePageRequestTarget))
		{
			throw new IllegalArgumentException("This encoder can only be used with "
					+ "instances of " + IBookmarkablePageRequestTarget.class.getName());
		}
		final AppendingStringBuffer url = new AppendingStringBuffer(40);
		url.append(getMountPath());
		final IBookmarkablePageRequestTarget target = (IBookmarkablePageRequestTarget)requestTarget;

		PageParameters pageParameters = target.getPageParameters();
		
		// If the target contains component path and request target interface, encode it.
		if (target instanceof BookmarkableListenerInterfaceRequestTarget) 
		{
			BookmarkableListenerInterfaceRequestTarget interfaceRequestTarget = (BookmarkableListenerInterfaceRequestTarget) target;
			final AppendingStringBuffer value = new AppendingStringBuffer(32);
			value.append(Component.PATH_SEPARATOR);
			value.append(interfaceRequestTarget.getComponentPath());
			value.append(Component.PATH_SEPARATOR);
			value.append(Component.PATH_SEPARATOR);
			value.append(interfaceRequestTarget.getInterfaceName());
			if (pageParameters == null)
			{
				pageParameters = new PageParameters();
			}
			pageParameters.put(WebRequestCodingStrategy.INTERFACE_PARAMETER_NAME, value);
		}
		
		String pagemap = pageMapName != null ? pageMapName : target.getPageMapName();
		if (pagemap != null)
		{
			if (pageParameters == null)
			{
				pageParameters = new PageParameters();
			}
			pageParameters.put(WebRequestCodingStrategy.PAGEMAP, pagemap);
		}
		appendParameters(url, pageParameters);
		return url;
	}

	/**
	 * @see wicket.request.target.coding.IRequestTargetUrlCodingStrategy#decode(wicket.request.RequestParameters)
	 */
	public IRequestTarget decode(RequestParameters requestParameters)
	{

		final String parametersFragment = requestParameters.getPath().substring(
				getMountPath().length());
		final PageParameters parameters = new PageParameters(decodeParameters(parametersFragment,
				requestParameters.getParameters()));
		String pageMapName = (String)parameters.remove(WebRequestCodingStrategy.PAGEMAP);
		if(requestParameters.getPageMapName() == null)
		{
			requestParameters.setPageMapName(pageMapName);
		}
		else
		{
			pageMapName = requestParameters.getPageMapName();
		}
		
		final BookmarkablePageRequestTarget target;
		
		final String bookmarkableInterfaceListener = (String) parameters.remove(
				WebRequestCodingStrategy.INTERFACE_PARAMETER_NAME);
		
		// Do the parameters contain component path and listener interface?
		if (bookmarkableInterfaceListener != null) {
			// try to parse component path and listener interface 
			final String[] pathComponents = Strings.split(bookmarkableInterfaceListener, Component.PATH_SEPARATOR);
			// There must be at least 4 path components
			if (pathComponents.length < 4)
			{
				throw new WicketRuntimeException("Internal error parsing "
						+ WebRequestCodingStrategy.INTERFACE_PARAMETER_NAME + " = " + bookmarkableInterfaceListener);
			}
			final String interfaceName = pathComponents[pathComponents.length - 1];
			final String componentPath = bookmarkableInterfaceListener.substring(1, 
					bookmarkableInterfaceListener.length() - interfaceName.length() - 2);
			
			target = new BookmarkableListenerInterfaceRequestTarget(pageMapName, bookmarkablePageClass, parameters,
					componentPath, interfaceName);
		}										
		else 
		{
			target = new BookmarkablePageRequestTarget(pageMapName,
					bookmarkablePageClass, parameters);	
		}
		 
		return target;
	}

	/**
	 * @see wicket.request.target.coding.IRequestTargetUrlCodingStrategy#matches(wicket.IRequestTarget)
	 */
	public boolean matches(IRequestTarget requestTarget)
	{
		if (requestTarget instanceof IBookmarkablePageRequestTarget)
		{
			IBookmarkablePageRequestTarget target = (IBookmarkablePageRequestTarget)requestTarget;
			if (bookmarkablePageClass.equals(target.getPageClass()))
			{
				if (this.pageMapName == null)
				{
					return true;
				}
				else
				{
					return this.pageMapName.equals(target.getPageMapName());
				}
			}
		}
		return false;
	}
}
