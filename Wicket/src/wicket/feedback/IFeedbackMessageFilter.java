/*
 * $Id: IFeedbackMessageFilter.java 3903 2006-01-19 19:57:34 +0000 (Thu, 19 Jan
 * 2006) joco01 $ $Revision: 5871 $ $Date: 2006-01-19 19:57:34 +0000 (Thu, 19
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
package wicket.feedback;

import java.io.Serializable;

/**
 * Interface for filtering feedback messages
 * 
 * @author Jonathan Locke
 */
public interface IFeedbackMessageFilter extends Serializable
{
	/**
	 * @param message
	 *            The message to test for inclusion
	 * @return True if the message should be included, false to exclude it
	 */
	boolean accept(FeedbackMessage message);
}