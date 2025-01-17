/*
 * $Id: NoCrypt.java 5883 2006-05-26 10:12:48Z joco01 $ $Revision: 5883 $
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
package wicket.util.crypt;


/**
 * Due to legal reasons in some countries the JRE is shipped without a security
 * provider. As a convenience solution, we provide a default implementation
 * which does not encrypt/decrypt the data. It does not modify the data at all.
 * Thus we strongly recommend not to use it for production sites.
 * 
 * @author Juergen Donnerstag
 */
public class NoCrypt implements ICrypt
{
	/**
	 * Constructor
	 */
	public NoCrypt()
	{
	}

	/**
	 * Decrypts a string into a string.
	 * 
	 * @param text
	 *            text to decript
	 * @return the decrypted text
	 */
	public final String decrypt(final String text)
	{
		return text;
	}

	/**
	 * Decrypts a string into a string.
	 * 
	 * @param text
	 *            text to decript
	 * @return the decrypted text
	 */
	public final String decryptUrlSafe(final String text)
	{
		return text;
	}

	/**
	 * Encrypt a string into a string
	 * 
	 * @param plainText
	 *            text to encrypt
	 * @return encrypted string
	 */
	public final String encrypt(final String plainText)
	{
		return plainText;
	}

	/**
	 * Encrypt a string into a string
	 * 
	 * @param plainText
	 *            text to encrypt
	 * @return encrypted string
	 */
	public final String encryptUrlSafe(final String plainText)
	{
		return plainText;
	}

	/**
	 * Set encryption private key
	 * 
	 * @param key
	 *            private key to make de-/encryption unique
	 */
	public void setKey(final String key)
	{
	}
}
