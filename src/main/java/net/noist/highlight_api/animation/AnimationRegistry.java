package net.noist.highlight_api.animation;

import net.noist.highlight_api.enums.AnimationType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AnimationRegistry {
    private static final Map<AnimationType, Supplier<ICustomAnimation>> registry = new HashMap<>();

    static {
        registry.put(AnimationType.PULSE, PulseAnimation::new);
        registry.put(AnimationType.BLINK, BlinkAnimation::new);
        registry.put(AnimationType.FADE_IN, FadeInAnimation::new);
    }

    public static ICustomAnimation create(AnimationType type) {
        Supplier<ICustomAnimation> supplier = registry.get(type);
        return supplier != null ? supplier.get() : null;
    }
}
