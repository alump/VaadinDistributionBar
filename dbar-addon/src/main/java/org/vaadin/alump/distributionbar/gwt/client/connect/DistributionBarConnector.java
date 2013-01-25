package org.vaadin.alump.distributionbar.gwt.client.connect;

import org.vaadin.alump.distributionbar.gwt.client.shared.DistributionBarState;
import org.vaadin.alump.distributionbar.gwt.client.GwtDistributionBar;

import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(org.vaadin.alump.distributionbar.DistributionBar.class)
public class DistributionBarConnector extends AbstractComponentConnector {

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

        getWidget().setNumberOfParts(getState().getParts().size());

        for (int i = 0; i < getState().getParts().size(); ++i) {
            DistributionBarState.Part part = getState().getParts().get(i);
            getWidget().setPartSize(i, part.getSize());
            getWidget().setPartTitle(i, part.getTitle());
            getWidget().setPartTooltip(i, part.getTooltip());
        }

        getWidget().updateParts();
    }
}
