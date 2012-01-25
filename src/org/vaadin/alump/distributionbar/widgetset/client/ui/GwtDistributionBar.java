/**
 * GwtDistributionBar.java (VaadinDistributionBar)
 * 
 * License:
 * Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * Copyright/Author:
 * 2012 Vaadin Ltd. Sami Viitanen <alump@vaadin.com>
 */
package org.vaadin.alump.distributionbar.widgetset.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * GwtDistributionBar is widget for presenting relative sizes of different
 * items. E.g. For presenting sized of Open, Assigned and Closed bugs. It will
 * show the size as number and the relative as width of the bar.
 */
public class GwtDistributionBar extends Widget {
	
	public static final String CLASSNAME = "alump-distributionbar";
	public static final String PARTCLASSNAMEPREFIX = CLASSNAME + "-part-";
	public static final String PART_TEXT_CLASSNAME = CLASSNAME + "-text";
	
	private final List<Integer> sizes;
	private Timer delayer;
	
	/**
	 * Default size of part before it is defined
	 */
	private static final int DEFAULT_VALUE = 0;
	
	public GwtDistributionBar() {
		
		initRootElement();
		
		Element element = Document.get().createSpanElement();
		element.setAttribute("class", CLASSNAME + "-uninitialized");
		element.setInnerText("uninitialized");
		
		getElement().appendChild(element);

		sizes = new ArrayList<Integer>();
		
		Window.addResizeHandler(resizeHandler);
	}
	
	private final ResizeHandler resizeHandler = new ResizeHandler() {
		
		public void onResize(final ResizeEvent event) {
			
			// This event handling must be delayed as parent has to be updated
			// first.
			if (delayer == null) {
				delayer = new Timer() {
					@Override
					public void run() {
			    	updateParts();
			      }
				};
			} else {
				delayer.cancel();
			}

			delayer.schedule(500);
		}
	};
	
	private Element initRootElement() {
		
		Element element = getElement();
		
	
		if (element != null) {
			
			Element child = null;
			while ((child = element.getFirstChildElement()) != null) {
				element.removeChild(child);
			}
		
		} else {
			element = Document.get().createDivElement();
			element.setAttribute("class", CLASSNAME);
			setElement (element);
		}
		
		return element;
	}
	
	/***
	 * Change the number of parts in distributionbar.
	 * @param parts Number of parts (min 2). If smaller value is given, it is
	 * converted to 2.
	 */
	public void setNumberOfParts (int parts) {
		
		if (parts == sizes.size()) {
			return;
		}
		
		sizes.clear();
		initRootElement();
		
		if (parts < 2) {
			parts = 2;
		}
		
		for (int i = 0; i < parts; ++i) {
			
			sizes.add(DEFAULT_VALUE);
			String styleName = CLASSNAME;
			
			//To round edges
			if (i == 0) {
				styleName += "-left";
			} else if (i == (parts - 1)) {
				styleName += "-right";
			} else {
				styleName += "-middle";
			}
			
			//This is for coloring
			styleName += " " + PARTCLASSNAMEPREFIX + String.valueOf(i+1);
			
			Element element = Document.get().createDivElement();
			element.setAttribute("class", styleName);
			
			Element textElem = Document.get().createSpanElement();
			textElem.setAttribute("class", PART_TEXT_CLASSNAME);
			textElem.setInnerText(String.valueOf(DEFAULT_VALUE));
			
			element.appendChild(textElem);
			getElement().appendChild(element);
			
		}
	}
	
	/***
	 * Sum of sizes of parts
	 * @return Sum of all sizes. Can be zero!
	 */
	public int totalSize () {
		int totalSize = 0;
		for (Integer value : sizes) {
			totalSize += value;
		}
		return totalSize;
	}
	
	/**
	 * Change size of part. Call updateParts after using this function to
	 * update the DOM structure.
	 * @param index Index of part [0...N]
	 * @param size Size as integer
	 * @param update If true updateParts is called
	 */
	public void setPartSize (int index, int size) {
		sizes.set(index, size);
	}
	
	/**
	 * Get root element of part with index
	 * @param index Index of part [0..N]. Use only valid indexes.
	 * @return Element of part.
	 */
	private Element getPartElement (int index) {
		Element element = getElement().getFirstChildElement();
		
		for (int i = 0; i < index && element != null; ++i) {
			element = element.getNextSiblingElement();
		}
		
		return element;
	}
	
	/**
	 * Change title attribute of part element. Call updateParts after using this
	 * function to update the DOM structure.
	 * @param index Index of part [0..N]. Use only valid indexes.
	 * @param title Title set to title attribute of part DIV element
	 */
	public void setPartTitle (int index, String title) {
				
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
	
	private void setPartElementSize (Element part, int size, int total,
		int parentSize) {
		
		double elementSize = 0.0f;
		Style.Unit elementSizeUnit = Unit.PX;
		
		if (parentSize <= 0) {
			elementSize = Math.floor(100.0f / sizes.size());
			elementSizeUnit = Unit.PCT;
		
		} else if (total == 0) {
			elementSize = Math.floor(parentSize / sizes.size());
			elementSizeUnit = Unit.PX;
			
		} else {
			final double minElementSize = 30.0f;
			final double minTotalSize = minElementSize * (sizes.size());
			final double availableSize = parentSize - minTotalSize;
			
			elementSize = minElementSize + (float)size / (float) total
				* availableSize;
		}
		
		part.getStyle().setWidth(elementSize, elementSizeUnit);
	}
	
	/**
	 * Update DOM presentation of distribution. This has to be called always
	 * after changes are done to parts.
	 */
	public void updateParts() {
		
		int totalSize = totalSize();
		
		Element element = getElement().getFirstChildElement();
		
		int totalWidth = getElement().getOffsetWidth();
			
		for (int i = 0; (i < sizes.size()) && (element != null); ++i) {
			
			int size = sizes.get(i);
			
			setPartElementSize (element, size, totalSize, totalWidth);
			
			Element textElem = element.getFirstChildElement();
			textElem.setInnerText(String.valueOf(size));
			
			Element nextElement = element.getNextSiblingElement();
			element = nextElement;
		}
	}

}
