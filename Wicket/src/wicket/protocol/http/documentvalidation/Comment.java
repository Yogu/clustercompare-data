/*
 * $Id: Comment.java 5883 2006-05-26 10:12:48Z joco01 $ $Revision: 5883 $
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
package wicket.protocol.http.documentvalidation;

/**
 * Class representing a comment in an HTML document.
 * 
 * @author Chris Turner
 */
public class Comment implements DocumentElement
{
	/** The text for this HTML comment */
	private String text;

	/**
	 * Create the comment.
	 * 
	 * @param text
	 *            The text for the comment
	 */
	public Comment(final String text)
	{
		this.text = text;
	}

	/**
	 * Get the comment text.
	 * 
	 * @return The text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * Output a descriptive string.
	 * 
	 * @return The string
	 */
	@Override
	public String toString()
	{
		return "[comment = '" + text + "']";
	}
}