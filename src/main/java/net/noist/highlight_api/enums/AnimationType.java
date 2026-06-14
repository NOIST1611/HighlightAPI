package net.noist.highlight_api.enums;

/**
 * Built-in animation presets for highlights.
 */
public enum AnimationType {
    /**
     * Smoothly pulses the highlight opacity in and out.
     */
    PULSE("pulse"),
    /**
     * Fades the highlight in from transparent to full opacity.
     */
    FADE_IN("fade_in"),
    /**
     * Rapidly blinks the highlight on and off.
     */
    BLINK("blink"),
    /**
     * No animation, the highlight is static.
     */
    NONE("none");

    private final String name;

    AnimationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}