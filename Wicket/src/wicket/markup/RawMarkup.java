/*
 * $Id: RawMarkup.java 5883 2006-05-26 10:12:48Z joco01 $ $Revision: 5883 $
 * $Date: 2006-05-26 12:12:48 +0200 (Fri, 26 May 2006) $
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

/**
 * This class is for framework purposes only, which is why the class is
 * (default) protected.
 * <p>
 * A RawMarkup element represents a hunk of unparsed HTML containing any
 * arbitrary content.
 * 
 * @see MarkupElement
 * @author Jonathan Locke
 */
public final class RawMarkup extends MarkupElement
{
	/** The raw markup string * */
	private final CharSequence string;

	/**
	 * Create a RawMarkup element referencing an uninterpreted markup string.
	 * 
	 * @param string
	 *            The raw markup
	 */
	public RawMarkup(final CharSequence string)
	{
		this.string = string;
	}

	/**
	 * Compare with a given object
	 * 
	 * @param o
	 *            The object to compare with
	 * @return True if equal
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (o instanceof String)
		{
			return string.equals(o);
		}

		if (o instanceof RawMarkup)
		{
			return string.equals(((RawMarkup)o).string);
		}

		return false;
	}

	/**
	 * We must override hashCode since we overrode equals.
	 * 
	 * @return Hashcode for this object
	 */
	@Override
	public int hashCode()
	{
		return string.hashCode();
	}

	/**
	 * @return This raw markup string
	 */
	@Override
	public String toString()
	{
		return string.toString();
	}

	/**
	 * @see wicket.markup.MarkupElement#toCharSequence()
	 */
	@Override
	public CharSequence toCharSequence()
	{
		return string;
	}

	/**
	 * @see MarkupElement#toUserDebugString()
	 */
	@Override
	public String toUserDebugString()
	{
		return "[Raw markup]";
	}
}
