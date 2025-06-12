package com.osrsbot.util;

import com.osrsbot.events.EventBus;

/**
 * Utility class for scripts to publish events or update overlays.
 */
public class BotUtils {
    public static <T> void publishEvent(T event) {
        EventBus.publish(event);
    }

    public static void overlayInfo(String info) {
        com.osrsbot.gui.OverlayManager.showInfo(info);
    }
}