/*
 * $Id: FileItemFactory.java 4710 2006-03-02 08:46:15 +0000 (Thu, 02 Mar 2006)
 * eelco12 $ $Revision: 5874 $ $Date: 2006-03-02 08:46:15 +0000 (Thu, 02 Mar
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
package wicket.util.upload;


/**
 * <p>
 * A factory interface for creating {@link FileItem} instances. Factories can
 * provide their own custom configuration, over and above that provided by the
 * default file upload implementation.
 * </p>
 * 
 * @author <a href="mailto:martinc@apache.org">Martin Cooper</a>
 */
public interface FileItemFactory
{

	/**
	 * Create a new {@link FileItem} instance from the supplied parameters and
	 * any local factory configuration.
	 * 
	 * @param fieldName
	 *            The name of the form field.
	 * @param contentType
	 *            The content type of the form field.
	 * @param isFormField
	 *            <code>true</code> if this is a plain form field;
	 *            <code>false</code> otherwise.
	 * @param fileName
	 *            The name of the uploaded file, if any, as supplied by the
	 *            browser or other client.
	 * 
	 * @return The newly created file item.
	 */
	FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName);
}
