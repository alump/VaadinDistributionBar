package org.vaadin.alump.distributionbar.gwt.client.connect;

import com.vaadin.shared.communication.ServerRpc;

public interface DistributionBarServerRpc extends ServerRpc {
	void onItemClicked(int index);
}
