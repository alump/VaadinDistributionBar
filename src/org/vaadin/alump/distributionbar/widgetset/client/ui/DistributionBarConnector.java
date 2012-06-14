package org.vaadin.alump.distributionbar.widgetset.client.ui;

import com.vaadin.terminal.gwt.client.communication.StateChangeEvent;
import com.vaadin.terminal.gwt.client.ui.AbstractComponentConnector;
import com.vaadin.terminal.gwt.client.ui.Connect;

@Connect(org.vaadin.alump.distributionbar.DistributionBar.class)
public class DistributionBarConnector extends AbstractComponentConnector {

    @Override
    public void init() {
        super.init();
        getWidget().setConnection(getConnection());
    }

    @Override
    public VDistributionBar getWidget() {
        return (VDistributionBar) super.getWidget();
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
