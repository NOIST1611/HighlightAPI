package net.noist.highlight_api.enums;

/**
 * Defines the target property of a highlight that will be affected by an animation.
 * <p>
 * When an {@link net.noist.highlight_api.animation.ICustomAnimation ICustomAnimation} or
 * built-in animation is active, it returns a float value (typically between 0.0 and 1.0).
 * This enum determines how that value is applied to the highlight's rendering.
 * </p>
 */
public enum AnimationScope {
    /**
     * Applies the animation value to the alpha (transparency) channel of the highlight.
     * <p>
     * For example, a value of 0.5 makes the highlight semi-transparent,
     * while 1.0 makes it fully opaque. RGB colors remain unchanged.
     * </p>
     */
    ALPHA,

    /**
     * Applies the animation value to the RGB (color) channels of the highlight.
     * <p>
     * For example, a value of 0.0 makes the highlight completely black,
     * while 1.0 restores its original color. The transparency (alpha) remains unchanged.
     * </p>
     */
    COLOR
}