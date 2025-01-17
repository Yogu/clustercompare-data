/*
 * $Id: FileUpload.java 5874 2006-05-25 22:46:21 +0000 (Thu, 25 May 2006)
 * eelco12 $ $Revision: 5881 $ $Date: 2006-05-25 22:46:21 +0000 (Thu, 25 May
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
 * High level API for processing file uploads.
 * </p>
 * 
 * <p>
 * This class handles multiple files per single HTML widget, sent using
 * <code>multipart/mixed</code> encoding type, as specified by <a
 * href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>.
 * </p>
 * 
 * <p>
 * How the data for individual parts is stored is determined by the factory used
 * to create them; a given part may be in memory, on disk, or somewhere else.
 * </p>
 * 
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:dlr@collab.net">Daniel Rall</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:jmcnally@collab.net">John McNally</a>
 * @author <a href="mailto:martinc@apache.org">Martin Cooper</a>
 * @author Sean C. Sullivan
 */
public class FileUpload extends FileUploadBase
{

	/**
	 * The factory to use to create new form items.
	 */
	private FileItemFactory fileItemFactory;

	/**
	 * Constructs an uninitialised instance of this class. A factory must be
	 * configured, using <code>setFileItemFactory()</code>, before attempting
	 * to parse requests.
	 * 
	 * @see #FileUpload(FileItemFactory)
	 */
	public FileUpload()
	{
		super();
	}


	/**
	 * Constructs an instance of this class which uses the supplied factory to
	 * create <code>FileItem</code> instances.
	 * 
	 * @param fileItemFactory
	 * 
	 * @see #FileUpload()
	 */
	public FileUpload(FileItemFactory fileItemFactory)
	{
		super();
		this.fileItemFactory = fileItemFactory;
	}

	/**
	 * Returns the factory class used when creating file items.
	 * 
	 * @return The factory class for new file items.
	 */
	@Override
	public FileItemFactory getFileItemFactory()
	{
		return fileItemFactory;
	}


	/**
	 * Sets the factory class to use when creating file items.
	 * 
	 * @param factory
	 *            The factory class for new file items.
	 */
	@Override
	public void setFileItemFactory(FileItemFactory factory)
	{
		this.fileItemFactory = factory;
	}
}
