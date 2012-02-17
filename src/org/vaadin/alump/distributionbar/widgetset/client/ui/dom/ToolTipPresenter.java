/**
 * GwtToolTipPresenter.java (DistributionBar)
 *
 * Copyright 2012 Sami Viitanen <sami.viitanen@gmail.com>
 * All rights reserved.
 */
package org.vaadin.alump.distributionbar.widgetset.client.ui.dom;

import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/**
 * Class that takes care of the tooltip presentation of Distribution Bar. Still
 * under development!
 */
public class ToolTipPresenter implements MouseOverHandler, MouseOutHandler,
        MouseMoveHandler {

    private final HashMap<Element, String> tooltips;
    private Element currentToolTip;
    private Element currentHoverElement;

    public ToolTipPresenter() {
        tooltips = new HashMap<Element, String>();
    }

    /**
     * Change tool tip content
     * 
     * @param element
     *            Element which tooltip is changed
     * @param content
     *            New content in XHTML
     */
    public void setToolTip(Element element, String content) {
        tooltips.put(element, content);
    }

    /**
     * Remove defined tooltip
     * 
     * @param element
     *            Element containing the tooltip
     */
    public void removeToolTip(Element element) {
        if (currentHoverElement == element) {
            removeCurrentToolTip();
        }
        tooltips.remove(element);
    }

    /**
     * Clear all tool tips defined. Useful if distribution bar is restructured.
     */
    public void clearAllToolTips() {
        removeCurrentToolTip();
        tooltips.clear();
    }

    public boolean hasToolTipForElement(Element element) {
        return tooltips.containsKey(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.event.dom.client.MouseOverHandler#onMouseOver(com.google
     * .gwt.event.dom.client.MouseOverEvent)
     */
    public void onMouseOver(MouseOverEvent event) {

        Element target = Element.as(event.getNativeEvent().getEventTarget());

        if (target == currentHoverElement) {
            return;
        } else if (hasToolTipForElement(target) == false) {
            removeCurrentToolTip();
            return;
        }

        removeCurrentToolTip();
        currentHoverElement = target;
        generateTooltip();

    }

    /**
     * Generate new tooltip to document. Make sure currentHoverElement is set
     * before this is called.
     */
    protected void generateTooltip() {

        currentToolTip = Document.get().createDivElement();
        currentToolTip.setClassName("alump-dbar-tooltip");

        currentToolTip.setInnerHTML(tooltips.get(currentHoverElement));

        Document.get().getBody().appendChild(currentToolTip);

        currentToolTip.getStyle().setPosition(Position.ABSOLUTE);

        changeTooltipPosition(currentHoverElement);
    }

    /**
     * Removes current tooltip from document
     */
    protected void removeCurrentToolTip() {
        if (currentToolTip != null) {
            currentToolTip.removeFromParent();
            currentToolTip = null;
            currentHoverElement = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.event.dom.client.MouseOutHandler#onMouseOut(com.google
     * .gwt.event.dom.client.MouseOutEvent)
     */
    public void onMouseOut(MouseOutEvent event) {

        // Always remove tooltip if we move outside
        removeCurrentToolTip();

    }

    /**
     * Adds isOrHasChild aspect to HashMap search
     * 
     * @param target
     *            Element searched (will accept if target is child of defined
     *            element).
     * @return Defined element or null if not found.
     */
    protected Element findMathingElement(Element target) {
        if (tooltips.containsKey(target)) {
            return target;
        } else {
            Iterator<Element> iter = tooltips.keySet().iterator();
            while (iter.hasNext()) {
                Element element = iter.next();
                if (element.isOrHasChild(target)) {
                    return element;
                }
            }
        }

        return null;
    }

    private void changeTooltipPosition(Element element) {

        int left = element.getAbsoluteLeft();
        int right = Document.get().getClientWidth()
                - element.getAbsoluteRight();

        if (element.getAbsoluteLeft() <= right) {
            currentToolTip.getStyle().setLeft(left, Unit.PX);
        } else {
            int width = currentToolTip.getOffsetWidth();

            currentToolTip.getStyle().setLeft(
                    element.getAbsoluteRight() - width, Unit.PX);
        }

        int top = element.getAbsoluteTop();
        int bottom = Document.get().getClientHeight()
                - element.getAbsoluteBottom();

        if (top <= bottom) {
            currentToolTip.getStyle().clearBottom();
            currentToolTip.getStyle().setTop(top + element.getOffsetHeight(),
                    Unit.PX);
        } else {
            currentToolTip.getStyle().clearTop();
            currentToolTip.getStyle().setBottom(
                    bottom + element.getOffsetHeight(), Unit.PX);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.event.dom.client.MouseMoveHandler#onMouseMove(com.google
     * .gwt.event.dom.client.MouseMoveEvent)
     */
    public void onMouseMove(MouseMoveEvent event) {
        Element target = Element.as(event.getNativeEvent().getEventTarget());

        if (target != currentHoverElement) {
            currentHoverElement = findMathingElement(target);
            if (currentHoverElement != null) {
                if (currentToolTip == null) {
                    generateTooltip();
                } else {

                    changeTooltipPosition(currentHoverElement);
                    currentToolTip.setInnerHTML(tooltips
                            .get(currentHoverElement));
                }
            } else {
                removeCurrentToolTip();
            }
        }

    }

}
