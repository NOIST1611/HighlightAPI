package net.noist.highlight_api;

import net.noist.highlight_api.animation.AnimationRegistry;
import net.noist.highlight_api.animation.ICustomAnimation;
import net.noist.highlight_api.enums.AnimationType;
import net.noist.highlight_api.enums.DepthMode;
import net.noist.highlight_api.enums.Lifetime;
import net.noist.highlight_api.enums.RenderMode;

/**
 * Builder for creating highlights in the world.
 * Chain methods and call {@link #register()} to finalize.
 */
public class HighlightBuilder {
    private float[] outlineColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    private float[] fillColor = new float[]{1.0f, 1.0f, 1.0f, 0.3f};
    private float lineWidth = 2.0f;
    private DepthMode depthMode = DepthMode.HIDE;
    private boolean visible = true;
    private AnimationType animation = AnimationType.NONE;
    private ICustomAnimation customAnimation;
    private float animationSpeed = 1.0f;
    private RenderMode renderMode = RenderMode.DEFAULT;
    private Object target;
    private Lifetime lifetime;
    private float delay = 0.0f;
    private Object targetEnd;

    public HighlightBuilder(Lifetime lifetime) {
        this.lifetime = Lifetime.ETERNAL;
    }

    public HighlightBuilder(Lifetime lifetime, float delay) {
        this.lifetime = Lifetime.DELAYED;
        this.delay = delay;
    }

    /**
     * Sets the target object to highlight.
     * Supported types: {@link net.minecraft.core.BlockPos}, {@link net.minecraft.world.entity.Entity}, {@link net.minecraft.world.phys.AABB}
     *
     * @param target the object to highlight
     */
    public <T> HighlightBuilder setTarget(T target) {
        this.target = target;
        return this;
    }

    /**
     * Sets the render mode for this highlight.
     *
     * @param renderMode the render mode to use
     * @see RenderMode
     */
    public HighlightBuilder setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
        return this;
    }

    /**
     * Sets the outline color of the highlight.
     *
     * @param r red component (0.0 - 1.0)
     * @param g green component (0.0 - 1.0)
     * @param b blue component (0.0 - 1.0)
     * @param a alpha component (0.0 - 1.0)
     */
    public HighlightBuilder setOutlineColor(float r, float g, float b, float a) {
        this.outlineColor = new float[]{r, g, b, a};
        return this;
    }

    /**
     * Sets the fill color of the highlight.
     *
     * @param r red component (0.0 - 1.0)
     * @param g green component (0.0 - 1.0)
     * @param b blue component (0.0 - 1.0)
     * @param a alpha component (0.0 - 1.0)
     */
    public HighlightBuilder setFillColor(float r, float g, float b, float a) {
        this.fillColor = new float[]{r, g, b, a};
        return this;
    }

    /**
     * Sets the line width of the outline.
     * Note: values above 1.0 may not work on all platforms.
     *
     * @param lineWidth the line width in pixels
     */
    public HighlightBuilder setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    /**
     * Sets the depth mode for this highlight.
     *
     * @param depthMode use {@link DepthMode#HIDE} to hide behind blocks or {@link DepthMode#IGNORE} to show through blocks
     */
    public HighlightBuilder setDepthMode(DepthMode depthMode) {
        this.depthMode = depthMode;
        return this;
    }

    /**
     * Sets the visibility of this highlight.
     *
     * @param visible true to show, false to hide
     */
    public HighlightBuilder setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    /**
     * Sets a preset animation for this highlight.
     *
     * @param animation the animation preset to use
     * @see AnimationType
     */
    public HighlightBuilder setAnimation(AnimationType animation) {
        this.animation = animation;
        this.customAnimation = AnimationRegistry.create(animation);
        return this;
    }

    /**
     * Sets a custom animation for this highlight.
     *
     * @param customAnimation the custom animation implementation
     * @see ICustomAnimation
     */
    public HighlightBuilder setAnimation(ICustomAnimation customAnimation) {
        this.customAnimation = customAnimation;
        this.animation = AnimationType.NONE;
        return this;
    }

    /**
     * Sets the animation speed multiplier.
     *
     * @param animationSpeed speed multiplier (1.0 is normal speed)
     */
    public HighlightBuilder setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
        return this;
    }

    /**
     * Sets the end target for {@link RenderMode#REGION} mode.
     * Used together with {@link #setTarget} to define a region between two points.
     *
     * @param targetEnd the end BlockPos of the region
     */
    public <T> HighlightBuilder setTargetEnd(T targetEnd) {
        this.targetEnd = targetEnd;
        return this;
    }

    /**
     * Finalizes and registers the highlight.
     *
     * @return a {@link HighlightHandle} to control the highlight after creation
     */
    public HighlightHandle register() {
        return new HighlightHandle(this);
    }

    public float[] getOutlineColor() { return outlineColor; }
    public float[] getFillColor() { return fillColor; }
    public float getLineWidth() { return lineWidth; }
    public DepthMode getDepthMode() { return depthMode; }
    public boolean isVisible() { return visible; }
    public AnimationType getAnimation() { return animation; }
    public ICustomAnimation getCustomAnimation() { return customAnimation; }
    public float getAnimationSpeed() { return animationSpeed; }
    public RenderMode getRenderMode() { return renderMode; }
    public Object getTarget() { return target; }
    public Lifetime getLifetime() { return lifetime; }
    public float getDelay() { return delay; }
    public Object getTargetEnd() { return targetEnd; }

}