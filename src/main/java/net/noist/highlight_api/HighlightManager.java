package net.noist.highlight_api;

import net.noist.highlight_api.enums.Lifetime;

import java.util.ArrayList;
import java.util.Iterator;

public class HighlightManager {
    private static final ArrayList<HighlightHandle> highlightHandlers = new ArrayList<>();

    public static void register(HighlightHandle highlight) {
        highlightHandlers.add(highlight);
    }

    public static void remove(HighlightHandle highlight) {
        highlightHandlers.remove(highlight);
    }

    public static ArrayList<HighlightHandle> getAllHandlers() {
        return highlightHandlers;
    }

    public static void tick(float deltaTime) {
        Iterator<HighlightHandle> iterator = highlightHandlers.iterator();

        while (iterator.hasNext()) {
            HighlightHandle highlightHandle = iterator.next();

            if (highlightHandle.getLifetime() == Lifetime.DELAYED) {
                highlightHandle.tickRemaining(deltaTime);
            }

            if (highlightHandle.isRemoved()) {
                iterator.remove();
            }
        }
    }
}
