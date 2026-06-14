package net.noist.highlight_api.animation;

/**
 * Interface for implementing custom highlight animations.
 * Return a float value between 0.0 and 1.0 representing the current alpha multiplier.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * public class MyAnimation implements ICustomAnimation {
 *     private float time = 0.0f;
 *
 *     @Override
 *     public float tick(float partialTick) {
 *         time += partialTick * 0.05f;
 *         return (float)(Math.sin(time) * 0.5 + 0.5);
 *     }
 * }
 * }</pre>
 */
public interface ICustomAnimation {
    /**
     * Called every frame to compute the current animation alpha.
     *
     * @param partialTick delta time between ticks for smooth animation
     * @return alpha multiplier between 0.0 (invisible) and 1.0 (fully visible)
     */
    float tick(float partialTick);
}
