/*
 * $Id: CoverterLocatorFactory.java 5775 2006-05-19 18:00:21 +0000 (Fri, 19 May
 * 2006) joco01 $ $Revision: 6946 $ $Date: 2006-05-19 18:00:21 +0000 (Fri, 19
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
package wicket.util.convert;

import wicket.IConverterLocator;

/**
 * Implementation of {@link IConverterLocatorFactory}which creates
 * an instance of {@link IConverterLocator}.
 * 
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public final class CoverterLocatorFactory implements IConverterLocatorFactory
{
	/**
	 * @see wicket.util.convert.IConverterLocatorFactory#newConverterSupplier()
	 */
	public IConverterLocator newConverterSupplier()
	{
		return new ConverterLocator();
	}
}