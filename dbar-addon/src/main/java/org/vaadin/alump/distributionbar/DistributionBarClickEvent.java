package org.vaadin.alump.distributionbar;

import com.vaadin.shared.MouseEventDetails;

/**
 * Event given to DistributionBarListener when bar is clicked
 */
public class DistributionBarClickEvent {

    private final DistributionBar distributionBar;

    private final int partIndex;

    private final MouseEventDetails details;

    public DistributionBarClickEvent(DistributionBar distributionBar, int clickIndex, MouseEventDetails details) {
        this.distributionBar = distributionBar;
        this.partIndex = clickIndex;
        this.details = details;
    }

    /**
     * Get clicked distribution bar
     * @return Distribution bar clicked
     */
    public DistributionBar getDistributionBar() {
        return distributionBar;
    }

    /**
     * Get index of part clicked
     * @return Index of part clicked
     */
    public int getPartIndex() {
        return partIndex;
    }

    /**
     * Cursor X in client's coordinate
     * @return X coordinate, or -1 if undefined
     */
    public int getClientX() {
        if (null != details) {
            return details.getClientX();
        } else {
            return -1;
        }
    }

    /**
     * Cursor Y in client's coordinate
     * @return Y coordinate, or -1 if undefined
     */
    public int getClientY() {
        if (null != details) {
            return details.getClientY();
        } else {
            return -1;
        }
    }

    /**
     * Cursor X in relative coordinate
     * @return X coordinate, or -1 if undefined
     */
    public int getRelativeX() {
        if (null != details) {
            return details.getRelativeX();
        } else {
            return -1;
        }
    }

    /**
     * Cursor Y in relative coordinate
     * @return Y coordinate, or -1 if undefined
     */
    public int getRelativeY() {
        if (null != details) {
            return details.getRelativeY();
        } else {
            return -1;
        }
    }

    /**
     * Check if Alt key was pressed down when clicked
     * @return true if Alt key was pressed down. false if not or undefined
     */
    public boolean isAltKey() {
        if (null != details) {
            return details.isAltKey();
        } else {
            return false;
        }
    }

    /**
     * Check if Ctrl key was pressed down when clicked
     * @return true if Ctrl key was pressed down. false if not or undefined
     */
    public boolean isCtrlKey() {
        if (null != details) {
            return details.isCtrlKey();
        } else {
            return false;
        }
    }

    /**
     * Check if Meta key was pressed down when clicked
     * @return true if Meta key was pressed down. false if not or undefined
     */
    public boolean isMetaKey() {
        if (null != details) {
            return details.isMetaKey();
        } else {
            return false;
        }
    }

    /**
     * Check if Shift key was pressed down when clicked
     * @return true if Shift key was pressed down. false if not or undefined
     */
    public boolean isShiftKey() {
        if (null != details) {
            return details.isShiftKey();
        } else {
            return false;
        }
    }
}