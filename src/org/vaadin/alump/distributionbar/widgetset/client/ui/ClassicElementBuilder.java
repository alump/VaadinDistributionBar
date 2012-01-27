/**
 * ClassicElementBuilder.java (VaadinDistributionBar)
 * 
 * Copyright 2012 Vaadin Ltd, Sami Viitanen <alump@vaadin.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.alump.distributionbar.widgetset.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;



/**
 * Replaces parts of element builder with html structure that works better
 * with legacy browsers (like IE7)
 */
public class ClassicElementBuilder extends ElementBuilder {
	
	protected static final String PART_TR_CLASSNAME = 
		GwtDistributionBar.CLASSNAME + "-tablerow";

	@Override
	protected Element initRootElement() {
		
		Element element = parent.getElement();
		
		if (element != null) {
			
			Element clearUnder = getParentElementForParts();
			Element child = null;
			while ((child = clearUnder.getFirstChildElement()) != null) {
				clearUnder.removeChild(child);
			}
		
		} else {
			element = Document.get().createTableElement();
			element.setAttribute("class", GwtDistributionBar.CLASSNAME);
			
			Element row = Document.get().createTRElement();
			row.setClassName(PART_TR_CLASSNAME);
			element.appendChild(row);
			
			parent.setRootElement (element);
		}
		
		return element;
	}
	
	@Override
	protected Element getParentElementForParts() {
		return parent.getElement().getFirstChildElement();
	}
		
	@Override
	protected void setPartElementWidth (Element element, double width,
		Style.Unit unit) {
			
		element.getStyle().setWidth(width, unit);
		
		//Don't trust just CSS
		if (unit == Style.Unit.PX) {
			element.setAttribute("width", String.valueOf((int)width));
		}
	}
	
	@Override
	protected Element createPartElement () {
		return Document.get().createTDElement();
	}
}
