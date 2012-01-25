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
 * Server side component for the VDistributionBar widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.alump.distributionbar.widgetset.client.ui.VDistributionBar.class)
public class DistributionBar extends AbstractComponent {
	
	private static final long serialVersionUID = 9179996093540029230L;
	private final List<Part> parts;
	
	private class Part {
		
		public Part(int size) {
			this.size = size;
			title = new String();
		}
		
		public int size;
		public String title;
	}
	
	public DistributionBar(int numberOfParts) {
		if (numberOfParts < 2) {
			numberOfParts = 2;
		}
		
		parts = new ArrayList<Part>();
		for (int i = 0; i < numberOfParts; ++i) {
			parts.add(new Part(0));
		}
	}
	
	public DistributionBar(int[] sizes) {
		if (sizes.length < 2) {
			sizes = new int[] { 0, 0 };
		}
		
		parts = new ArrayList<Part>();
		for (int i = 0; i < sizes.length; ++i) {
			parts.add(new Part(sizes[i]));
		}
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		
		target.addAttribute("parts", parts.size());
		for (int i = 0; i < parts.size(); ++i) {
			Part part = parts.get(i);
			target.addAttribute ("partsize-" + String.valueOf(i),
				part.size);
			target.addAttribute ("parttitle-" + String.valueOf(i),
				part.title);
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
	
	public void updatePartSizes (int[] partSizes) {

		for (int i = 0; i < partSizes.length; ++i) {
			parts.get(i).size = partSizes[i];
		}
		
		requestRepaint();
	}
	
	public void setPartSize (int index, int size) {
		Part part = parts.get(index); 
		part.size = size;
		requestRepaint();
	}
	
	public void setPartTitle (int index, String title) {
		Part part = parts.get(index); 
		part.title = title;
		requestRepaint();
	}

}
