/**
 * IE6ElementBuilder.java (VaadinDistributionBar)
 *
 * Copyright 2012 Sami Viitanen <sami.viitanen@gmail.com>
 * All rights reserved.
 */
package org.vaadin.alump.distributionbar.widgetset.client.ui.dom;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

/**
 * This will disable Distribution bar when using IE6
 */
public class IE6ElementBuilder extends ElementBuilder {
	
	@Override
	public Element initRootElement() {
		Element element = parent.getElement();
		
		// Only init once
		if (element == null) {
			element = Document.get().createDivElement();
			element.setAttribute("class", UNINITIALIZED_VALUE_CLASSNAME);
			element.setInnerText(
				"Please upgrade Internet Exploder to 8.0 or newer.");
			parent.setRootElement (element);
		}
		
		return element;		
	}
	
	@Override
	public void addUninitializedWarning() {
		//Do nothing
	}
	
	@Override
	public void updateParts(List<Integer> sizes) {
		//Do nothing
	}
	
	@Override
	public void addPartElement (int index, int parts, int size,
		String title) {
		//Do nothing
	}
	
	@Override
	public void changePartTitle(int index, String title) {
		//Do nothing
	}

}
