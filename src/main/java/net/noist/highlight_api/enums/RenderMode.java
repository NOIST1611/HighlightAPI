package net.noist.highlight_api.enums;

import net.noist.highlight_api.HighlightBuilder;

/**
 * Defines the rendering mode for a highlight.
 */
public enum RenderMode {
    /**
     * Highlights a single object (block, entity, or AABB).
     */
    REGION,
    /**
     * Highlights a region between two BlockPos points.
     * Requires both {@link HighlightBuilder#setTarget} and {@link HighlightBuilder#setTargetEnd} to be set.
     */
    DEFAULT
}
