/*
 * $Id: ICompoundModel.java 5861 2006-05-25 20:55:07 +0000 (Thu, 25 May 2006)
 * eelco12 $ $Revision: 6181 $ $Date: 2006-05-25 20:55:07 +0000 (Thu, 25 May
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
package wicket.model;

import wicket.Component;

/**
 * This is a marker interface for models that can be inherited from components
 * higher in the hierarchy
 * 
 * If a model implements this interface then you can give the parent container
 * this model and all the child (recursively) components will also get and then
 * set that model on their own if they are created with a null model
 * 
 * <pre>
 * Form form = new Form(&quot;form&quot;, new ModelImplementingICompoundModel());
 * form.add(new TextField(&quot;textfield&quot;)); // notice textfield is created with a null model
 * </pre>
 * 
 * @param <T>
 *            Type of model object this model holds
 * 
 * @author jcompagner
 * @author Igor Vaynberg (ivaynberg)
 */
public interface IInheritableModel<T> extends IModel<T>
{
	/**
	 * @param <C>
	 *            The type of the component
	 * @param component
	 * @return The WrapModel that wraps this model
	 */
	<C> IWrapModel<C> wrapOnInhertance(Component<C> component);
}