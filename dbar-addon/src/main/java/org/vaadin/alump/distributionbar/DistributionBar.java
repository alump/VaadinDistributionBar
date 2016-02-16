/**
 * DistributionBar.java (DistributionBar)
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
package org.vaadin.alump.distributionbar;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.MouseEventDetails;
import org.vaadin.alump.distributionbar.gwt.client.shared.DistributionBarServerRpc;
import org.vaadin.alump.distributionbar.gwt.client.shared.DistributionBarState;
import org.vaadin.alump.distributionbar.gwt.client.shared.DistributionBarState.Part;

import com.vaadin.ui.AbstractComponent;

/**
 * Server side component for the VDistributionBar widget. Distribution Bar is
 * simple graphical bar that can be used to show distribution of items between
 * different groups. For example distribution of YES and NO votes. Bar must have
 * at least two values, but there is not any upper limit. Layout where bar is
 * used can limit the amount of parts that will fit to screen.
 */
@SuppressWarnings("serial")
public class DistributionBar extends AbstractComponent {
	
	private List<DistributionBarClickListener> clickListeners = new ArrayList<DistributionBarClickListener>();

    private final DistributionBarServerRpc serverRpc = new DistributionBarServerRpc() {
        @Override
        public void onItemClicked(int index, MouseEventDetails mouseEventDetails) {
            DistributionBarClickEvent event = new DistributionBarClickEvent(DistributionBar.this, index, mouseEventDetails);

            for (DistributionBarClickListener listener : clickListeners) {
                listener.onDistributionBarClicked(event);
            }
        }
    };

    /**
     * Will make distribution bar with 2 parts (size value zero).
     */
    public DistributionBar() {
        this(2);
    }

    /**
     * Generate distribution bar with given number of parts. All parts created
     * will get the default size value: zero.
     * 
     * @param numberOfParts
     *            Number of parts. Must be 1 or more.
     */
    public DistributionBar(int numberOfParts) {

        if(numberOfParts < 1) {
            throw new IllegalArgumentException("Distribution bar must have at least one part: " + numberOfParts
                    + " parts given");
        }

    	registerRpc(serverRpc);
        for (int i = 0; i < numberOfParts; ++i) {
            getState().getParts().add(new Part());
        }

    }

    /**
     * Create new distribution bar and define sizes for all parts
     * 
     * @param sizes
     *            Part sizes in integer array. Must have at least 2 sizes. If
     *            less two parts are made with size zero.
     */
    public DistributionBar(final double[] sizes) {
        this(sizes.length);
        this.updatePartSizes(sizes);
    }

    @Override
    public DistributionBarState getState() {
        return (DistributionBarState) super.getState();
    }

    @Override
    public DistributionBarState getState(boolean markDirty) {
        return (DistributionBarState) super.getState(markDirty);
    }

    /**
     * Update multiple sizes once. If given list is smaller than number of parts
     * then parts at the end will not be updated. If given list has more sizes
     * than there is parts, then rest of the sizes are ignored.
     * 
     * @param partSizes
     *            Sizes in integer array
     */
    public void updatePartSizes(double[] partSizes) {
        for (int i = 0; i < getNumberOfParts() && i < partSizes.length; ++i) {
            getState().getParts().get(i).setSize(partSizes[i]);
        }
    }

    /**
     * Setup part by defining both size and tooltip with one command
     * 
     * @param index
     *            Index of part [0..N]. Only give valid indexes.
     * @param size
     *            Size as integer number
     * @param tooltip
     *            Tooltip content is XHTML
     */
    public void setupPart(int index, double size, String tooltip) {
        setupPart(index, size, tooltip, null);
    }

    /**
     * Setup part by defining size, tooltip and style name with one command
     *
     * @param index
     *            Index of part [0..N]. Only give valid indexes.
     * @param size
     *            Size of part (0.0 or larger)
     * @param tooltip
     *            Tooltip content is XHTML
     * @param styleName
     *            Stylename added to part
     */
    public void setupPart(int index, double size, String tooltip, String styleName) {

        Part part = getState().getParts().get(index);
        part.setSize(size);
        part.setTooltip(tooltip);
        part.setTooltip(styleName);
    }

    /**
     * Change size of given part
     * 
     * @param index
     *            Index of part [0..N]. Only give valid indexes.
     * @param size
     *            Size of part (0.0 or larger)
     * @return Reference to DistributionBar to allow call chaining
     */
    public DistributionBar setPartSize(int index, double size) {
        if(size < 0.0) {
            throw new IllegalArgumentException("Size must be zero or larger");
        }
        getState().getParts().get(index).setSize(size);
        return this;
    }

    /**
     * Change both size and caption of part
     * @param index
     *            Index of part [0..N]. Only give valid indexes.
     * @param size
     *            Size of part (0.0 or larger)
     * @param caption
     *            Caption of part (null to show value)
     * @return Reference to DistributionBar to allow call chaining
     */
    public DistributionBar setPartSize(int index, double size, String caption) {
        if(size < 0.0) {
            throw new IllegalArgumentException("Size must be zero or larger (" + size + ")");
        }
        Part part = getState().getParts().get(index);
        part.setSize(size);
        part.setCaption(caption);

        return this;
    }

    /**
     * Get current size of part
     * @param index Index of part
     * @return Size of part (0 or larger)
     * @throws IndexOutOfBoundsException If invalid index is given
     */
    public double getPartSize(int index) throws IndexOutOfBoundsException {
        return getState(false).getParts().get(index).getSize();
    }

    /**
     * Change title of given part. This is normal HTML title attribute. For many
     * use cases tooltip is better option.
     * 
     * @param index
     *            Index of part [0..N]. Only give valid indexes.
     * @param title
     *            Title for part
     * @return Reference to DistributionBar to allow call chaining
     */
    public DistributionBar setPartTitle(int index, String title) {
        getState().getParts().get(index).setTitle(title);
        return this;
    }

    /**
     * Get current caption of part. If not null, will replace number in bar element.
     * @param index Index of part
     * @return Caption of part, null if size of part is used
     * @throws IndexOutOfBoundsException If invalid index is given
     */
    public String getPartCaption(int index) {
        return getState(false).getParts().get(index).getTitle();
    }

    /**
     * Change caption of given part.
     *
     * @param index
     *            Index of part [0..N]. Only give valid indexes.
     * @param caption
     *            Caption of part (null to show value)
     * @return Reference to DistributionBar to allow call chaining
     *
     */
    public DistributionBar setPartCaption(int index, String caption) {
        getState().getParts().get(index).setCaption(caption);
        return this;
    }

    /**
     * Get current title of part
     * @param index Index of part
     * @return Title of part
     * @throws IndexOutOfBoundsException If invalid index is given
     */
    public String getPartTitle(int index) {
        return getState().getParts().get(index).getTitle();
    }

    /**
     * Change tooltip of given part.
     * 
     * @param index
     *            Index of part [0..N]. Only give valid indexes.
     * @param tooltip
     *            Content of tooltip (empty is do not show tooltip). Content is
     *            given in XHTML format.
     * @return Reference to DistributionBar to allow call chaining
     */
    public DistributionBar setPartTooltip(int index, String tooltip) {
        getState(false).getParts().get(index).setTooltip(tooltip);
        return this;
    }

    /**
     * Change stylename of given part.
     *
     * @param index
     *            Index of part [0..N]. Only give valid indexes.
     * @param styleName
     *            Style name of part
     * @return Reference to DistributionBar to allow call chaining
     */
    public DistributionBar setPartStyleName(int index, String styleName) {
        getState().getParts().get(index).setStyleName(styleName);
        return this;
    }

    /**
     * Get number of parts in distribution bar
     * 
     * @return Number of parts in distribution bar
     */
    public int getNumberOfParts() {
        return getState().getParts().size();
    }

    private void changeStatePartsSize(int newSize) {

        while (getState().getParts().size() < newSize) {
            getState().getParts().add(new Part());
        }
        while (getState().getParts().size() > newSize) {
            getState().getParts().remove(newSize);
        }
    }

    /**
     * Change number of parts in distribution bar.
     * 
     * @param numberOfParts
     *            New number of parts. If this is different than earlier number
     *            of parts then all parts will be initialized to default state
     *            with size zero. If given value is less than 2 it will be
     *            converted to two.
     */
    public void setNumberOfParts(int numberOfParts) {
        if (numberOfParts > 1 && getNumberOfParts() != numberOfParts) {
            changeStatePartsSize(numberOfParts);
        }
    }
	
	/**
	 * Add click listener
	 * @param listener Listener added to listeners
	 */
	public void addDistributionBarClickListener(
			DistributionBarClickListener listener) {
		
		if (clickListeners.isEmpty()) {
			getState().sendClicks = true;
		}
		
		clickListeners.add(listener);
	}
	
	/**
	 * Remove click listener
	 * @param listener Listener removed from listeners
	 */
	public void removeDistributionBarClickListener(
			DistributionBarClickListener listener) {
		clickListeners.remove(listener);
		
		if (clickListeners.isEmpty()) {
			getState().sendClicks = false;
		}
	}

    /**
     * Define if parts with size 0 should still be shown in distribution bar
     * @param zeroVisible true if zero sized are shown, false if shrunk to invisible
     */
    public void setZeroSizedVisible(boolean zeroVisible) {
        getState().zeroVisible = zeroVisible;
    }

    /**
     * See if zero sized parts are shown or shrunk to invisible
     * @return true if zero sized are shown, false if shrunk to invisible
     */
    public boolean isZeroSizedVisible() {
        return getState().zeroVisible;
    }

    /**
     * Define minimum with of part with value. Value will be overridden if there isn't enough space.
     * @param pixels With in pixels.
     */
    public void setMinPartWidth(double pixels) {
        getState().minWidth = pixels;
    }

    /**
     * Get minimum width of part with value.
     * @return Minimum with of part in pixels.
     */
    public double getMinPartWidth() {
        return getState().minWidth;
    }
}
