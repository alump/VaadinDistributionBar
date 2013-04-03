package org.vaadin.alump.distributionbar.gwt.client.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.AbstractComponentState;

@SuppressWarnings("serial")
public class DistributionBarState extends AbstractComponentState {

	public boolean sendClicks = false;
    private List<Part> parts = new ArrayList<Part>();

    /**
     * Internal storage class for part details
     */
    public static class Part implements Serializable {

        private int size;
        private String title;
        private String tooltip;

        public Part() {
            title = new String();
            tooltip = new String();
        }

        public Part(int size) {
            setSize(size);
            title = new String();
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTooltip(String tooltip) {
            this.tooltip = tooltip;
        }

        public String getTooltip() {
            return tooltip;
        }
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}
