package org.vaadin.alump.distributionbar.gwt.client.connect;

import org.vaadin.alump.distributionbar.gwt.client.shared.DistributionBarState;
import org.vaadin.alump.distributionbar.gwt.client.GwtDistributionBar;
import org.vaadin.alump.distributionbar.gwt.client.GwtDistributionBar.ClickListener;

import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(org.vaadin.alump.distributionbar.DistributionBar.class)
public class DistributionBarConnector extends AbstractComponentConnector implements ClickListener {
	
	protected final DistributionBarServerRpc serverRpc = RpcProxy.create(
			DistributionBarServerRpc.class, this);
	private boolean clicksConnected = false;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public GwtDistributionBar getWidget() {
        return (GwtDistributionBar) super.getWidget();
    }

    @Override
    public DistributionBarState getState() {
        return (DistributionBarState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        
        // Only connect to clicks when requested
        if (clicksConnected != getState().sendClicks) {
        	if (getState().sendClicks) {
        		getWidget().addClickListener(this);
        		clicksConnected = true;
        	} else {
        		getWidget().removeClickListener(this);
        		clicksConnected = false;
        	}
        }

        getWidget().setNumberOfParts(getState().getParts().size());

        for (int i = 0; i < getState().getParts().size(); ++i) {
            DistributionBarState.Part part = getState().getParts().get(i);
            getWidget().setPartSize(i, part.getSize());
            getWidget().setPartTitle(i, part.getTitle());
            getWidget().setPartTooltip(i, part.getTooltip());
        }

        getWidget().updateParts();
    }

	@Override
	public void onItemClicked(int index) {
		serverRpc.onItemClicked(index);
	}
}
