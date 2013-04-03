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

import java.util.LinkedList;
import java.util.List;

import org.vaadin.alump.distributionbar.gwt.client.connect.DistributionBarServerRpc;
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
public class DistributionBar extends AbstractComponent implements DistributionBarServerRpc {
	
	private List<DistributionBarClickListener> clickListeners = new LinkedList<DistributionBarClickListener>();
	
	public interface DistributionBarClickListener {
		void onItemClicked(int index);
	}

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
     *            Number of parts. Must be two or more. Smaller values are
     *            converted to two.
     */
    public DistributionBar(int numberOfParts) {

    	registerRpc(this);
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
    public DistributionBar(final int[] sizes) {

        this(sizes.length);

        this.updatePartSizes(sizes);
    }

    @Override
    public DistributionBarState getState() {
        return (DistributionBarState) super.getState();
    }

    /**
     * Update multiple sizes once. If given list is smaller than number of parts
     * then parts at the end will not be updated. If given list has more sizes
     * than there is parts, then rest of the sizes are ignored.
     * 
     * @param partSizes
     *            Sizes in integer array
     */
    public void updatePartSizes(int[] partSizes) {
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
    public void setupPart(int index, int size, String tooltip) {
        Part part = getState().getParts().get(index);
        part.setSize(size);
        part.setTooltip(tooltip);
    }

    /**
     * Change size of given part
     * 
     * @param index
     *            Index of part [0..N]. Only give valid indexes.
     * @param size
     *            Size as integer number
     */
    public void setPartSize(int index, int size) {
        getState().getParts().get(index).setSize(size);
    }

    /**
     * Change title of given part. This is normal HTML title attribute. For many
     * use cases tooltip is better option.
     * 
     * @param index
     *            Index of part [0..N]. Only give valid indexes.
     * @param title
     *            Title for part
     */
    public void setPartTitle(int index, String title) {
        getState().getParts().get(index).setTitle(title);
    }

    /**
     * Change tooltip of given part.
     * 
     * @param index
     *            Index of part [0..N]. Only give valid indexes.
     * @param tooltip
     *            Content of tooltip (empty is do not show tooltip). Content is
     *            given in XHTML format.
     */
    public void setPartTooltip(int index, String tooltip) {
        getState().getParts().get(index).setTooltip(tooltip);
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

	@Override
	public void onItemClicked(int index) {
		for (DistributionBarClickListener listener : clickListeners) {
			listener.onItemClicked(index);
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
}
