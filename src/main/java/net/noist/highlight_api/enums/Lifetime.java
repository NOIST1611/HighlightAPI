package net.noist.highlight_api.enums;

import net.noist.highlight_api.HighlightAPI;
import net.noist.highlight_api.HighlightHandle;

/**
 * Defines the lifetime of a highlight.
 */
public enum Lifetime {
    /**
     * The highlight exists until explicitly removed via {@link HighlightHandle#remove()}.
     */
    ETERNAL,

    /**
     * The highlight is automatically removed after a specified delay in seconds.
     * Set the delay via {@link HighlightAPI#create(Lifetime, float)}.
     */
    DELAYED
}