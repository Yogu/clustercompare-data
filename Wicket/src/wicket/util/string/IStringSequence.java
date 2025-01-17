/*
 * $Id: IStringSequence.java 3585 2006-01-02 07:37:31 +0000 (Mon, 02 Jan 2006)
 * jonathanlocke $ $Revision: 5874 $ $Date: 2006-01-02 07:37:31 +0000 (Mon, 02
 * Jan 2006) $
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
package wicket.util.string;

/**
 * Typesafe interface to an ordered sequence of strings. An IStringIterator can
 * be retrieved for the sequence by calling iterator(), the number of Strings in
 * the sequence can be determined by calling size() and a given String can be
 * retrieved by calling get(int index).
 * 
 * @author Jonathan Locke
 */
public interface IStringSequence
{
	/**
	 * Gets a string at a given index in the sequence
	 * 
	 * @param index
	 *            The index
	 * @return The string at the given index
	 * @throws IndexOutOfBoundsException
	 */
	String get(int index) throws IndexOutOfBoundsException;

	/**
	 * @return Typesafe string iterator
	 */
	IStringIterator iterator();

	/**
	 * @return Number of strings in this sequence
	 */
	int size();
}
