package net.noist.highlight_api.animation;

public class PulseAnimation implements ICustomAnimation {
    private float time = 0.0f;

    @Override
    public float tick(float partialTick) {
        time += partialTick * 0.05f;
        return (float)(Math.sin(time) * 0.5 + 0.5);
    }
}