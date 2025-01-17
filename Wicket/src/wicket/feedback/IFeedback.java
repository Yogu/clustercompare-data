/*
 * $Id: IFeedback.java 2455 2005-08-13 06:10:54 +0000 (Sat, 13 Aug 2005)
 * jonathanlocke $ $Revision: 5871 $ $Date: 2005-08-13 06:10:54 +0000 (Sat, 13
 * Aug 2005) $
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

/**
 * Interface for components that present some kind of feedback to the user,
 * normally based on the feedback messages attached to various components on a
 * given page.
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 */
public interface IFeedback
{
	/**
	 * This method is called on any component implementing IFeedback when it is
	 * time for the component to update its feedback display
	 */
	void updateFeedback();
}
