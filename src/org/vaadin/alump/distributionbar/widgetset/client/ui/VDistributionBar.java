/**
 * VDistributionBar.java (VaadinDistributionBar)
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

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

/**
 * Client side of DistributionBar which communicates with the server.
 */
public class VDistributionBar extends GwtDistributionBar implements Paintable {

	protected String paintableId;
	protected ApplicationConnection client;
	
	private static final String ATTR_PARTS = "parts";
	private static final String ATTR_PREFIX_PARTSIZE = "partsize-";
	private static final String ATTR_PREFIX_PARTTITLE = "parttitle-";

	public VDistributionBar() {
	}

    /**
     * Called whenever an update is received from the server 
     */
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if (client.updateComponent(this, uidl, true)) {
			return;
		}

		this.client = client;
		paintableId = uidl.getId();

		if (uidl.hasAttribute(ATTR_PARTS)) {
			int parts = uidl.getIntAttribute(ATTR_PARTS);
			setNumberOfParts (parts);
		
			for (int i = 0; i < parts; ++i) {
				final String indexStr = String.valueOf(i);
				
				String attribute = ATTR_PREFIX_PARTSIZE + indexStr;
				if (uidl.hasAttribute(attribute)) {
					int size = uidl.getIntAttribute(attribute);
					setPartSize (i, size);
				}
				
				attribute = ATTR_PREFIX_PARTTITLE + indexStr;
				if (uidl.hasAttribute(attribute)) {
					setPartTitle (i, uidl.getStringAttribute(attribute));
				}
			}
			
			updateParts();
		}
	}
}
