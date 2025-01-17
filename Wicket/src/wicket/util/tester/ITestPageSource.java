/*
 * $Id: ITestPageSource.java 3585 2006-01-02 07:37:31 +0000 (Mon, 02 Jan 2006)
 * jonathanlocke $ $Revision: 6229 $ $Date: 2006-01-02 07:37:31 +0000 (Mon, 02
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
package wicket.util.tester;

import java.io.Serializable;

import wicket.Page;

/**
 * A test page factory for WicketTester
 * 
 * @author Ingram Chen
 */
public interface ITestPageSource extends Serializable
{
	/**
	 * Define a page instance source for WicketTester
	 * 
	 * @return Page created page instance for testing
	 */
	Page getTestPage();
}
