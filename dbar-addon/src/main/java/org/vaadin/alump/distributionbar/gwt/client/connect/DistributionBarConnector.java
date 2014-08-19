package org.vaadin.alump.distributionbar.gwt.client.connect;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
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

    public final int DELAYED_UPDATE_PARTS_MS = 400;

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
            getWidget().setPartStyleName(i, part.getStyleName());
        }

        getWidget().updateParts();

        // As Vaadin layouts will have delayed sizing, this makes sure size is double checked
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                getWidget().updateParts();
            }
        });
    }

	@Override
	public void onItemClicked(int index) {
		serverRpc.onItemClicked(index);
	}
}
