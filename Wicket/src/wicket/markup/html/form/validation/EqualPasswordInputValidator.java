/*
 * $Id: EqualInputValidator.java 5301 2006-04-08 23:48:26 +0200 (za, 08 apr
 * 2006) joco01 $ $Revision: 5301 $ $Date: 2006-04-08 23:48:26 +0200 (za, 08 apr
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
package wicket.markup.html.form.validation;

import wicket.markup.html.form.FormComponent;

/**
 * Validates that the input of two form components is identical. Errors are
 * reported on the second form component with key 'EqualInputValidator' and the
 * variables:
 * <ul>
 * <li>${input(n)}: the user's input</li>
 * <li>${name}: the name of the component</li>
 * <li>${label(n)}: the label of the component - either comes from
 * FormComponent.labelModel or resource key [form-id].[form-component-id] in
 * that order</li>
 * </ul>
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public class EqualPasswordInputValidator extends EqualInputValidator
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Construct.
	 * 
	 * @param formComponent1
	 * @param formComponent2
	 */
	public EqualPasswordInputValidator(FormComponent formComponent1, FormComponent formComponent2)
	{
		super(formComponent1, formComponent2);
	}


}