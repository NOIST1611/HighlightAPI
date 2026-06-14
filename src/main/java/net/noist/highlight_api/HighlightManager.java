package net.noist.highlight_api;

import net.noist.highlight_api.enums.Lifetime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HighlightManager {
    private static final CopyOnWriteArrayList<HighlightHandle> highlightHandlers = new CopyOnWriteArrayList<>();

    public static void register(HighlightHandle highlight) {
        highlightHandlers.add(highlight);
    }

    public static void remove(HighlightHandle highlight) {
        highlightHandlers.remove(highlight);
    }

    public static CopyOnWriteArrayList<HighlightHandle> getAllHandlers() {
        return highlightHandlers;
    }

    public static void tick(float deltaTime) {
        for (int i = highlightHandlers.size() - 1; i >= 0; i--) {
            HighlightHandle highlightHandle = highlightHandlers.get(i);

            if (highlightHandle.getLifetime() == Lifetime.DELAYED) {
                highlightHandle.tickRemaining(deltaTime);
            }

            if (highlightHandle.isRemoved()) {
                highlightHandlers.remove(i);
            }
        }
    }
}
