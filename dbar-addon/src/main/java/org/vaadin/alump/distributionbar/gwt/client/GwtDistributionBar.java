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
import java.util.Map;

import com.google.gwt.event.shared.HandlerRegistration;
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
import org.vaadin.alump.distributionbar.gwt.client.dom.ToolTipPresenter;

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
    private final List<Double> sizes;

    /**
     * Default size of part before it is defined
     */
    private static final double DEFAULT_VALUE = 0.0;

    /**
     * Default title used
     */
    private static final String DEFAULT_TITLE = "";

    /**
     * Instance that will take care of all DOM tree manipulations
     */
    transient private ElementBuilder builder;
    
    private boolean eventsConnected = false;

    private HandlerRegistration windowResizeReg;

    public final static int DELAYED_UPDATE_MS = 300;

    private boolean zeroVisible = true;

    private double minPartWidth = 30.0;

    private String tooltipStyleNames = "";

    private ToolTipPresenter.TooltipClassNameProvider tooltipClassNameProvider;
    
    /**
     * Interface for click listeners
     */
    public interface ClickListener {
    	/**
    	 * Called when item is clicked.
    	 * @param index Index of item clicked.
         * @param event Click event
    	 */
    	void onItemClicked(int index, Event event);
    }
    
    /**
     * Click listeners
     */
    private List<ClickListener> clickListeners = new LinkedList<ClickListener>();

    /**
     * Constructor
     */
    public GwtDistributionBar() {

        sizes = new ArrayList<Double>();

        // Make sure builder is initialized when constructor is called
        getBuilder();
    }

    public void setTooltipClassNameProvider(ToolTipPresenter.TooltipClassNameProvider provider) {
        tooltipClassNameProvider = provider;
    }
    
    public void onAttach() {
    	super.onAttach();
    	updateParts();
    	
    	connectClickHandlingIfNeeded();

        windowResizeReg = Window.addResizeHandler(resizeHandler);
    }

    public void onDetach() {
        if(windowResizeReg != null) {
            windowResizeReg.removeHandler();
            windowResizeReg = null;
        }
        super.onDetach();
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
							listener.onItemClicked(i, event);
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

        if(tooltipClassNameProvider != null) {
            builder.setTooltipClassNameProvider(tooltipClassNameProvider);
        }

        return builder;
    }

    /**
     * Handled for resize events of window
     */
    private final ResizeHandler resizeHandler = new ResizeHandler() {

        public void onResize(final ResizeEvent event) {
            updateParts();
        }
    };

    /***
     * Change the number of parts in distributionbar.
     * 
     * @param parts
     *            Number of parts (min 1).
     */
    public void setNumberOfParts(int parts) {

        if(parts < 1) {
            throw new IllegalArgumentException("Bar needs to have at least one part");
        }

        if (parts == sizes.size()) {
            return;
        }

        sizes.clear();
        getBuilder().initRootElement();

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
    public double totalSize() {
        double totalSize = 0;
        for (Double value : sizes) {
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
     */
    public void setPartSize(int index, double size) {
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

    public void setPartStyleName(int index, int styleIndex, String styleName) {
        getBuilder().changePartStyleName(index, styleIndex, sizes.size(), styleName);
    }

    /**
     * Update part widths by updating the DOM three
     */
    public void updateParts() {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                if(GwtDistributionBar.this.isAttached()) {
                    updateParts(true);
                }
            }
        });
    }

    /**
     * Sync update with optional extra delayed async update
     * @param runDelayed if true extra update is run with some delay
     */
    public void updateParts(boolean runDelayed) {

        callBuilderToUpdateParts();

        if(runDelayed) {
            Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
                @Override
                public boolean execute() {
                    if(GwtDistributionBar.this.isAttached()) {
                        callBuilderToUpdateParts();
                    }
                    return false;
                }
            }, DELAYED_UPDATE_MS);
        }
    }

    private void callBuilderToUpdateParts() {
        getBuilder().updateParts(sizes, minPartWidth);
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

    public void setMinPartWidth(double minWidth) {
        minPartWidth = minWidth;
    }

    public double getMinPartWidth() {
        return minPartWidth;
    }

}
