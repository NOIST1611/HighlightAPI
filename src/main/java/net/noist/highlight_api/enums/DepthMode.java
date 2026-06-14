package net.noist.highlight_api.enums;

/**
 * Defines how the highlight is rendered relative to other geometry.
 */
public enum DepthMode {
    /**
     * The highlight is hidden behind blocks and other geometry.
     */
    HIDE,

    /**
     * The highlight is visible through blocks and other geometry.
     */
    IGNORE
}