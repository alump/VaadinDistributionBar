/**
 * GwtDistributionBar.java (DistributionBar)
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
package org.vaadin.alump.distributionbar.gwt.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.vaadin.alump.distributionbar.gwt.client.dom.ElementBuilder;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * GwtDistributionBar is widget for presenting relative sizes of different
 * items. E.g. For presenting sized of Open, Assigned and Closed bugs. It will
 * show the size as number and the relative as width of the bar.
 */
public class GwtDistributionBar extends Widget {

    /**
     * Class name
     */
    public static final String CLASSNAME = "alump-dbar";

    /**
     * List of sizes of parts
     */
    private final List<Integer> sizes;

    /**
     * Default size of part before it is defined
     */
    private static final int DEFAULT_VALUE = 0;

    /**
     * Default title used
     */
    private static final String DEFAULT_TITLE = new String();

    /**
     * Instance that will take care of all DOM tree manipulations
     */
    transient private ElementBuilder builder;
    
    private boolean eventsConnected = false;
    
    /**
     * Interface for click listeners
     */
    public interface ClickListener {
    	/**
    	 * Called when item is clicked.
    	 * @param index Index of item clicked.
    	 */
    	void onItemClicked(int index);
    }
    
    /**
     * Click listeners
     */
    private List<ClickListener> clickListeners = new LinkedList<ClickListener>();

    /**
     * Constructor
     */
    public GwtDistributionBar() {

        sizes = new ArrayList<Integer>();

        // Make sure builder is initialized when constructor is called
        getBuilder();

        Window.addResizeHandler(resizeHandler);
    }
    
    public void onAttach() {
    	super.onAttach();
    	updateParts();
    	
    	connectClickHandlingIfNeeded();
    }
    
    private void connectClickHandlingIfNeeded() {
    	if (!clickListeners.isEmpty() && !eventsConnected) {
    		DOM.sinkEvents(getElement(), Event.ONCLICK);
    		DOM.setEventListener(getElement(), eventListener);
    		eventsConnected = true;
    	}
    }
    
    private EventListener eventListener = new EventListener() {

		@Override
		public void onBrowserEvent(Event event) {
			if (event.getTypeInt() == Event.ONCLICK && !clickListeners.isEmpty()) {
				Element target = Element.as(event.getEventTarget());
				for (int i = 0; i < getElement().getChildCount(); ++i) {
					Element child = Element.as(getElement().getChild(i));
					if (child.isOrHasChild(target)) {
						for (ClickListener listener : clickListeners) {
							listener.onItemClicked(i);
						}
						event.stopPropagation();
						event.preventDefault();
						return;
					}
				}
			}
		}
    	
    };

    private ElementBuilder getBuilder() {
        if (builder == null) {
            builder = new ElementBuilder();
            builder.setParent(this);
            builder.initRootElement();
            builder.addUninitializedWarning();
        }

        return builder;
    }

    /**
     * Handled for resize events of window
     */
    private final ResizeHandler resizeHandler = new ResizeHandler() {

        public void onResize(final ResizeEvent event) {

            Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                public void execute() {
                    builder.updateParts(sizes);
                }

            });
        }
    };

    /***
     * Change the number of parts in distributionbar.
     * 
     * @param parts
     *            Number of parts (min 2). If smaller value is given, it is
     *            converted to 2.
     */
    public void setNumberOfParts(int parts) {

        if (parts < 2) {
            parts = 2;
        }

        if (parts == sizes.size()) {
            return;
        }

        sizes.clear();
        getBuilder().initRootElement();

        if (parts < 2) {
            parts = 2;
        }

        for (int i = 0; i < parts; ++i) {

            sizes.add(DEFAULT_VALUE);

            builder.addPartElement(i, parts, DEFAULT_VALUE, DEFAULT_TITLE);
        }
    }

    /***
     * Sum of sizes of parts
     * 
     * @return Sum of all sizes. Can be zero!
     */
    public int totalSize() {
        int totalSize = 0;
        for (Integer value : sizes) {
            totalSize += value;
        }
        return totalSize;
    }

    /**
     * Change size of part. Call updateParts after using this function to update
     * the DOM structure.
     * 
     * @param index
     *            Index of part [0...N]
     * @param size
     *            Size as integer
     * @param update
     *            If true updateParts is called
     */
    public void setPartSize(int index, int size) {
        sizes.set(index, size);
    }

    /**
     * Change title attribute of part element. Call updateParts after using this
     * function to update the DOM structure.
     * 
     * @param index
     *            Index of part [0..N]. Use only valid indexes.
     * @param title
     *            Title set to title attribute of part DIV element
     */
    public void setPartTitle(int index, String title) {
        getBuilder().changePartTitle(index, title);
    }

    /**
     * Change tooltip attribute of part element.
     * 
     * @param index
     *            Index of part which tooltip is changed
     * @param content
     *            Tooltip content in XHTML
     */
    public void setPartTooltip(int index, String content) {
        getBuilder().changePartTooltip(index, content);
    }

    public void setPartStyleName(int index, String styleName) {
        getBuilder().changePartStyleName(index, sizes.size(), styleName);
    }

    /**
     * Update part widths by updating the DOM three
     */
    public void updateParts() {
        getBuilder().updateParts(sizes);
    }

    /**
     * This to allow builders to change the structure of widget. Only to be
     * called by ElementBuilder or classes extending it.
     * 
     * @param element
     *            New root element
     */
    public void setRootElement(Element element) {
        setElement(element);
    }
    
    /**
     * Add new ClickListener
     * @param listener
     */
    public void addClickListener(ClickListener listener) {
    	clickListeners.add(listener);
    	connectClickHandlingIfNeeded();
    }
    
    /**
     * Remove ClickListener
     * @param listener
     */
    public void removeClickListener(ClickListener listener) {
    	clickListeners.remove(listener);
    }

}
