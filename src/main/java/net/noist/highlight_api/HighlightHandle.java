package net.noist.highlight_api;

import net.noist.highlight_api.animation.AnimationRegistry;
import net.noist.highlight_api.animation.ICustomAnimation;
import net.noist.highlight_api.enums.AnimationType;
import net.noist.highlight_api.enums.DepthMode;
import net.noist.highlight_api.enums.Lifetime;
import net.noist.highlight_api.enums.RenderMode;

/**
 * A handle to control a registered highlight.
 * All setters apply changes immediately without needing to call any update method.
 * Obtain an instance via {@link HighlightBuilder#register()}.
 */
public class HighlightHandle {
    private float[] outlineColor;
    private float[] fillColor;
    private float lineWidth;
    private DepthMode depthMode;
    private boolean visible;
    private AnimationType animation;
    private ICustomAnimation customAnimation;
    private float animationSpeed;
    private boolean removed;
    private Object target;
    private RenderMode renderMode;
    private float remainingTime;
    private Lifetime lifetime;
    private Object targetEnd;

    public HighlightHandle(HighlightBuilder builder) {
        this.outlineColor = builder.getOutlineColor();
        this.fillColor = builder.getFillColor();
        this.lineWidth = builder.getLineWidth();
        this.depthMode = builder.getDepthMode();
        this.visible = builder.isVisible();
        this.animation = builder.getAnimation();
        this.customAnimation =  builder.getCustomAnimation();
        this.animationSpeed = builder.getAnimationSpeed();
        this.target = builder.getTarget();
        this.renderMode = builder.getRenderMode();
        this.removed = false;
        this.remainingTime = builder.getDelay();
        this.lifetime = builder.getLifetime();
        this.targetEnd = builder.getTargetEnd();


        HighlightManager.register(this);
    }

    /**
     * Sets the outline color of the highlight.
     *
     * @param r red component (0.0 - 1.0)
     * @param g green component (0.0 - 1.0)
     * @param b blue component (0.0 - 1.0)
     * @param a alpha component (0.0 - 1.0)
     */
    public HighlightHandle setOutlineColor(float r, float g, float b, float a) {
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
    public HighlightHandle setFillColor(float r, float g, float b, float a) {
        this.fillColor = new float[]{r, g, b, a};
        return this;
    }

    /**
     * Sets the line width of the outline.
     *
     * @param lineWidth the line width in pixels
     */
    public HighlightHandle setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    /**
     * Sets the depth mode for this highlight.
     *
     * @param depthMode the depth mode to use
     * @see DepthMode
     */
    public HighlightHandle setDepthMode(DepthMode depthMode) {
        this.depthMode = depthMode;
        return this;
    }


    /**
     * Sets the visibility of this highlight.
     *
     * @param visible true to show, false to hide
     */
    public HighlightHandle setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    /**
     * Sets a preset animation for this highlight.
     *
     * @param animation the animation preset to use
     * @see AnimationType
     */
    public HighlightHandle setAnimation(AnimationType animation) {
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
    public HighlightHandle setAnimation(ICustomAnimation customAnimation) {
        this.customAnimation = customAnimation;
        this.animation = AnimationType.NONE;
        return this;
    }

    /**
     * Sets the animation speed multiplier.
     *
     * @param animationSpeed speed multiplier (1.0 is normal speed)
     */
    public HighlightHandle setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
        return this;
    }

    public void tickRemaining(float deltaTime) {
        this.remainingTime -= deltaTime;

        if (remainingTime <= 0) {
            this.removed = true;
        }
    }

    /**
     * Sets the end target for {@link RenderMode#REGION} mode.
     * Used together with setTarget to define a region between two points.
     *
     * @param targetEnd the end BlockPos of the region
     */
    public HighlightHandle setTargetEnd(Object targetEnd) {
        this.targetEnd = targetEnd;
        return this;
    }

    /**
     * Removes this highlight from the world.
     * After calling this method the highlight will no longer be rendered.
     */
    public void remove() {
        this.removed = true;
    }

    public float[] getOutlineColor() { return outlineColor; }
    public float[] getFillColor() { return fillColor; }
    public float getLineWidth() { return lineWidth; }
    public DepthMode getDepthMode() { return depthMode; }
    public boolean isVisible() { return visible; }
    public AnimationType getAnimation() { return animation; }
    public ICustomAnimation getCustomAnimation() { return customAnimation; }
    public float getAnimationSpeed() { return animationSpeed; }
    public boolean isRemoved() { return removed; }
    public Object getTarget() { return target; }
    public RenderMode getRenderMode() { return renderMode; }
    public float getRemainingTime() { return remainingTime; }
    public Lifetime getLifetime() { return lifetime; }
    public Object getTargetEnd() { return targetEnd; }
}