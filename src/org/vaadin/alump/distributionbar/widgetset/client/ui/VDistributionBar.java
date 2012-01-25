/**
 * VDistributionBar.java (VaadinDistributionBar)
 * 
 * License:
 * Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * Copyright/Author:
 * 2012 Vaadin Ltd. Sami Viitanen <alump@vaadin.com>
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
