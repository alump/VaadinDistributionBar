package org.vaadin.alump.distributionbar.gwt.client.connect;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Event;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.shared.MouseEventDetails;
import org.vaadin.alump.distributionbar.gwt.client.dom.ToolTipPresenter.TooltipClassNameProvider;
import org.vaadin.alump.distributionbar.gwt.client.shared.DistributionBarServerRpc;
import org.vaadin.alump.distributionbar.gwt.client.shared.DistributionBarState;
import org.vaadin.alump.distributionbar.gwt.client.GwtDistributionBar;
import org.vaadin.alump.distributionbar.gwt.client.GwtDistributionBar.ClickListener;

import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
@Connect(org.vaadin.alump.distributionbar.DistributionBar.class)
public class DistributionBarConnector extends AbstractComponentConnector implements ClickListener {
	
	protected final DistributionBarServerRpc serverRpc = RpcProxy.create(
			DistributionBarServerRpc.class, this);
	private boolean clicksConnected = false;

    public final int DELAYED_UPDATE_PARTS_MS = 400;

    /**
     * Map used to fix indexed when zero parts are removed before widget. Widget index to original index map.
     */
    private Map<Integer,Integer> indexRepair;

    @Override
    public void init() {
        super.init();
        getWidget().setTooltipClassNameProvider(new TooltipClassNameProvider() {

            @Override
            public String getClassNames() {
                return DistributionBarConnector.this.getConnection().getUIConnector().getWidget().getParent().getStyleName();
            }
        });
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

        List<DistributionBarState.Part> parts = getCleanedParts(getState().getParts());
        getWidget().setNumberOfParts(parts.size());

        for (int i = 0; i < parts.size(); ++i) {
            DistributionBarState.Part part = parts.get(i);
            getWidget().setPartSize(i, part.getSize(), part.getCaption());
            getWidget().setPartTitle(i, part.getTitle());
            getWidget().setPartTooltip(i, part.getTooltip());
            if(indexRepair != null) {
                getWidget().setPartStyleName(i, indexRepair.get(i), part.getStyleName());
            } else {
                getWidget().setPartStyleName(i, i, part.getStyleName());
            }
        }

        getWidget().setMinPartWidth(getState().minWidth);

        getWidget().updateParts();

        // As Vaadin layouts will have delayed sizing, this makes sure size is double checked
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                getWidget().updateParts();
            }
        });
    }

    protected List<DistributionBarState.Part> getCleanedParts(List<DistributionBarState.Part> parts) {
        if(getState().zeroVisible) {
            indexRepair = null;
            return parts;
        }

        List<DistributionBarState.Part> cleaned = new ArrayList<DistributionBarState.Part>();
        indexRepair = new HashMap<Integer,Integer>();

        for(int i = 0; i < parts.size(); ++i) {
            DistributionBarState.Part part = parts.get(i);
            if(part.getSize() > 0.0) {
                cleaned.add(part);
                indexRepair.put(cleaned.size() - 1, i);
            }
        }

        if(cleaned.isEmpty()) {
            indexRepair = null;
            return parts;
        }

        return cleaned;
    }

	@Override
	public void onItemClicked(int index, Event event) {
        MouseEventDetails details = MouseEventDetailsBuilder
                .buildMouseEventDetails(event, getWidget()
                        .getElement());

        if(indexRepair != null) {
            serverRpc.onItemClicked(indexRepair.get(index), details);
        } else {
            serverRpc.onItemClicked(index, details);
        }
	}
}
