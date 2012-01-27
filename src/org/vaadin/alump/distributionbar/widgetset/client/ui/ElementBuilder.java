/**
 * ElementBuilder.java (VaadinDistributionBar)
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

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Element builder contains all DOM tree functionality of GwtDistributionBar.
 * This to allow overriding functionality for different browsers.
 */
public class ElementBuilder {
	
	protected static final String PART_CLASSNAME_PREFIX =
		GwtDistributionBar.CLASSNAME + "-part-";
	protected static final String PART_VALUE_CLASSNAME =
		GwtDistributionBar.CLASSNAME + "-value";
	protected static final String UNINITIALIZED_VALUE_CLASSNAME =
			GwtDistributionBar.CLASSNAME + "-uninitizalized";
	
	protected GwtDistributionBar parent;
	
	public ElementBuilder() {
		
	}
	
	/**
	 * This function must be called after constructor. It's left out of
	 * constructor to allow replacement creation easily with GWT.create()
	 * @param parent Parent using builder. Used to access DOM tree.
	 */
	protected void setParent (GwtDistributionBar parent) {
		this.parent = parent;
	}
	
	protected int getFullWidth () {
		return parent.getElement().getOffsetWidth();
	}
	
	/**
	 * Get element where parts are added
	 * @return
	 */
	protected Element getParentElementForParts() {
		return parent.getElement();
	}
	
	/**
	 * Other part elements must be siblings to this element. If not you have to
	 * override functions above this.
	 * @return Element for first part which has other parts as siblings
	 */
	protected Element getFirstPartElement () {
		return getParentElementForParts().getFirstChildElement();
	}
	
	/**
	 * Initialize root element of distribution bar
	 * @return Root element initialized
	 */
	protected Element initRootElement() {
		
		Element element = parent.getElement();
		
		if (element != null) {
			
			Element child = null;
			while ((child = element.getFirstChildElement()) != null) {
				element.removeChild(child);
			}
		
		} else {
			element = Document.get().createDivElement();
			element.setAttribute("class", GwtDistributionBar.CLASSNAME);
			parent.setRootElement (element);
		}
		
		return element;
	}
	
	protected String generatePartStyleName (int index, int parts) {
		String styleName = GwtDistributionBar.CLASSNAME;
		
		// Allows styling like rounded corners
		if (index == 0) {
			styleName += "-left";
		} else if (index == (parts - 1)) {
			styleName += "-right";
		} else {
			styleName += "-middle";
		}
		
		styleName += " " + PART_CLASSNAME_PREFIX + String.valueOf(index+1);
		
		return styleName;
	}
	
	/**
	 * Adds new DOM element under widget
	 * @param index Index for added part
	 * @param parts Number of parts
	 * @param size Size of part added
	 * @param title Title of part added
	 * @return Element of part added
	 */
	protected Element addPartElement (int index, int parts, int size,
		String title) {
		
		String styleName = generatePartStyleName (index, parts);
		
		Element element = createPartElement ();
		element.setAttribute("class", styleName);
		element.setAttribute("title", title);
		
		Element textElem = Document.get().createSpanElement();
		textElem.setAttribute("class", PART_VALUE_CLASSNAME);
		textElem.setInnerText(String.valueOf(size));
		
		element.appendChild(textElem);
		
		getParentElementForParts().appendChild(element);
		
		return element;
	}
	
	/**
	 * Add warning text shown if distribution bar is used uninitialized
	 */
	protected void addUninitializedWarning() {
		
		Element element = createPartElement ();
		
		Element content = Document.get().createSpanElement();
		content.setAttribute("class", UNINITIALIZED_VALUE_CLASSNAME);
		content.setInnerText("uninitialized");
		element.appendChild(content);
		
		getParentElementForParts().appendChild(element);
	}
	
	/**
	 * Get root element of part with index
	 * @param index Index of part [0..N]. Use only valid indexes.
	 * @return Element of part.
	 */
	protected Element getPartElement (int index) {
		Element element = getFirstPartElement ();
		
		for (int i = 0; i < index && element != null; ++i) {
			element = element.getNextSiblingElement();
		}
		
		return element;
	}
	
	protected void changePartTitle(int index, String title) {
		Element element = getPartElement (index);
		
		if (element != null) {
			if (title.isEmpty()) {
				if (element.hasAttribute("title")) {
					element.removeAttribute("title");
				}
			} else {
				String safe = SafeHtmlUtils.htmlEscape(title);
				element.setAttribute("title", safe);
			}
		}
	}
	
	/**
	 * Update DOM presentation of distribution. This has to be called always
	 * after changes are done to parts.
	 */
	protected void updateParts(List<Integer> sizes) {
		
		int totalSize = parent.totalSize();
		
		Element element = getFirstPartElement();
		int totalWidth = getFullWidth ();
			
		for (int i = 0; (i < sizes.size()) && (element != null); ++i) {
			
			int size = sizes.get(i);
			
			setPartElementSize (element, size, totalSize, sizes.size(),
				totalWidth);
			
			Element nextElement = element.getNextSiblingElement();
			element = nextElement;
		}
	}
	
	/**
	 * Set size (width) for part element
	 * @param part Part element
	 * @param size New size
	 * @param total Sum of all sizes
	 * @param part How many parts there are
	 * @param parentSize Size of parent in pixels
	 */
	protected void setPartElementSize (Element part, double size, double total,
		double parts, double parentSize) {
		
		double elementSize = 0.0;
		Style.Unit elementSizeUnit = Unit.PX;
		
		if (parentSize <= 0) {
			elementSize = Math.floor(100.0 / parts);
			elementSizeUnit = Unit.PCT;
		
		} else if (total == 0) {
			elementSize = Math.floor(parentSize / parts);
			elementSizeUnit = Unit.PX;
			
		} else {
			final double minElementSize = 30.0;
			final double minTotalSize = minElementSize * parts;
			final double availableSize = parentSize - minTotalSize;
			
			elementSize = minElementSize + size / total
				* availableSize;
		}
		
		setPartElementWidth (part, elementSize, elementSizeUnit);
		part.getStyle().setWidth(elementSize, elementSizeUnit);
		setPartElementValueText (part, String.valueOf(size));
	}
	
	protected void setPartElementWidth (Element element, double width,
		Style.Unit unit) {
		
		element.getStyle().setWidth(width, unit);
	}
	
	protected Element createPartElement () {
		return Document.get().createDivElement();
	}
	
	/**
	 * Change the text presentation of part size in part element
	 * @param element Part element given
	 * @param text Text shown
	 */
	protected void setPartElementValueText (Element element, String text) {
		Element textElem = element.getFirstChildElement();
		textElem.setInnerText(text);
	}

}
