package net.noist.highlight_api.animation;

public class FadeInAnimation implements ICustomAnimation {
    private float time = 0.0f;

    @Override
    public float tick(float partialTick) {
        time += partialTick * 0.05f;
        return Math.min(time, 1.0f);
    }
}