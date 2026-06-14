package net.noist.highlight_api.animation;

public class BlinkAnimation implements ICustomAnimation {
    private float time = 0.0f;

    @Override
    public float tick(float partialTick) {
        time += partialTick * 0.05f;
        return (float)(Math.sin(time * 5.0f)) > 0 ? 1.0f : 0.0f;
    }
}