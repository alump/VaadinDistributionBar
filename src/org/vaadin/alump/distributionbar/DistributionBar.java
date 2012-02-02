/**
 * DistributionBar.java (VaadinDistributionBar)
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
import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

/**
 * Server side component for the VDistributionBar widget. Distribution Bar is
 * simple graphical bar that can be used to show distribution of items between
 * different groups. For example distribution of YES and NO votes. Bar must have
 * at least two values, but there is not any upper limit. Layout where bar is
 * used can limit the amount of parts that will fit to screen.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.alump.distributionbar.widgetset.client.ui.VDistributionBar.class)
public class DistributionBar extends AbstractComponent {
	
	private static final long serialVersionUID = -3581161316003689470L;
	private List<Part> parts;
	
	/**
	 * Internal storage class for part details
	 */
	private static class Part {
		
		private int size;
		private String title;
		private String tooltip;
		
		public Part() {
			title = new String();
			tooltip = new String();
		}
		
		@SuppressWarnings("unused")
		public Part(int size) {
			setSize (size);
			title = new String();
		}
		
		public void setSize (int size) {
			this.size = Math.abs(size);
		}
		
		public int getSize () {
			return size;
		}
		
		public void setTitle (String title) {
			this.title = title;
		}
		
		public String getTitle () {
			return title;
		}
		
		public void setTooltip (String tooltip) {
			this.tooltip = tooltip;
		}
		
		public String getTooltip () {
			return tooltip;
		}
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
	 * @param numberOfParts Number of parts. Must be two or more. Smaller
	 * values are converted to two.
	 */
	public DistributionBar(int numberOfParts) {
		parts = createPartsList(numberOfParts);
	}
	
	/**
	 * Create new distribution bar and define sizes for all parts
	 * @param sizes Part sizes in integer array. Must have at least 2 sizes. If
	 * less two parts are made with size zero.
	 */
	public DistributionBar(final int[] sizes) {
		
		parts = createPartsList(sizes.length);
		
		for (int i = 0; i < sizes.length; ++i) {
			parts.get(i).size = sizes[i];
		}
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		
		target.addAttribute("parts", parts.size());
		for (int i = 0; i < parts.size(); ++i) {
			Part part = parts.get(i);
			target.addAttribute ("psize-" + String.valueOf(i),
				part.getSize());
			target.addAttribute ("ptitle-" + String.valueOf(i),
				part.getTitle());
			target.addAttribute ("ptooltip-" + String.valueOf(i),
				part.getTooltip());
		}
	}

	/**
	 * Receive and handle events and other variable changes from the client.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		super.changeVariables(source, variables);
	}
	
	/**
	 * Update multiple sizes once. If given list is smaller than number of
	 * parts then parts at the end will not be updated. If given list has more
	 * sizes than there is parts, then rest of the sizes are ignored.
	 * @param partSizes Sizes in integer array
	 */
	public void updatePartSizes (int[] partSizes) {
		
		for (int i = 0; i < partSizes.length && i < parts.size(); ++i) {
			parts.get(i).size = partSizes[i];
		}
		
		requestRepaint();
	}
	
	public void setupPart (int index, int size, String tooltip) {
		Part part = parts.get(index); 
		part.setSize(size);
		part.setTooltip(tooltip);
		requestRepaint();
	}
	
	/**
	 * Change size of given part
	 * @param index Index of part [0..N]. Only give valid indexes.
	 * @param size Size as integer number
	 */
	public void setPartSize (int index, int size) {
		Part part = parts.get(index); 
		part.setSize(size);
		requestRepaint();
	}
	
	/**
	 * Change title of given part. This is normal HTML title attribute. For
	 * many use cases tooltip is better option.
	 * @param index Index of part [0..N]. Only give valid indexes.
	 * @param title Title for part
	 */
	public void setPartTitle (int index, String title) {
		Part part = parts.get(index); 
		part.setTitle(title);
		requestRepaint();
	}
	
	/**
	 * Change tooltip of given part.
	 * @param index Index of part [0..N]. Only give valid indexes.
	 * @param tooltip Content of tooltip (empty is do not show tooltip). Content
	 * is given in XHTML format.
	 */
	public void setPartTooltip (int index, String tooltip) {
		Part part = parts.get(index); 
		part.setTooltip(tooltip);
		requestRepaint();
	}
	
	/**
	 * Get number of parts in distribution bar
	 * @return Number of parts in distribution bar
	 */
	public int getNumberOfParts() {
		return parts.size();
	}
	
	/**
	 * Change number of parts in distribution bar.
	 * @param numberOfParts New number of parts. If this is different than
	 * earlier number of parts then all parts will be initialized to default
	 * state with size zero. If given value is less than 2 it will be converted
	 * to two.
	 */
	public void setNumberOfParts(int numberOfParts) {		
		if (parts.size() == numberOfParts) {
			return;
		} else if (parts.size() != numberOfParts) {
			parts = createPartsList(numberOfParts);
			requestRepaint();
		}
	}
	
	/**
	 * Generate new parts lists. All part information is lost.
	 * @param amount Amount of parts in list. If less than two is given value
	 * is read as two.
	 * @return Parts list generated
	 */
	private static List<Part> createPartsList(int amount) {
		int makeParts = amount;
		if (makeParts < 2) {
			makeParts = 2;
		}
		
		List<Part> ret = new ArrayList<Part>();
		for (int i = 0; i < makeParts; ++i) {
			ret.add(new Part());
		}
		
		return ret;
	}

}
