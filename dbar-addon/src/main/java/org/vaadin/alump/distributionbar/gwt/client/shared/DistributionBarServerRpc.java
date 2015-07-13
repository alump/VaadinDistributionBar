package org.vaadin.alump.distributionbar.gwt.client.shared;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

public interface DistributionBarServerRpc extends ServerRpc {
	void onItemClicked(int index, MouseEventDetails mouseEventDetails);
}
