/*
 * $Id: BooleanConverter.java 5791 2006-05-20 00:32:57 +0000 (Sat, 20 May 2006)
 * joco01 $ $Revision: 5873 $ $Date: 2006-05-20 00:32:57 +0000 (Sat, 20 May
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
package wicket.util.convert.converters;

import java.util.Locale;

import wicket.util.convert.IConverter;
import wicket.util.string.StringValueConversionException;
import wicket.util.string.Strings;

/**
 * Converts from Object to Boolean.
 * 
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public final class BooleanConverter extends AbstractConverter
{
	private static final long serialVersionUID = 1L;

	/**
	 * The singleton instance for a boolean converter
	 */
	public static final IConverter INSTANCE = new BooleanConverter();

	/**
	 * @see wicket.util.convert.IConverter#convertToObject(java.lang.String,Locale)
	 */
	public Object convertToObject(final String value, Locale locale)
	{
		try
		{
			return Strings.toBoolean(value);
		}
		catch (StringValueConversionException e)
		{
			throw newConversionException("Cannot convert '" + value + "' to Boolean", value, locale);
		}
	}

	/**
	 * @see wicket.util.convert.converters.AbstractConverter#getTargetType()
	 */
	@Override
	protected Class getTargetType()
	{
		return Boolean.class;
	}

}