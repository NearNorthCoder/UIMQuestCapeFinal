package com.osrsbot.util;

import com.osrsbot.events.EventBus;

/**
 * Utility class for scripts to publish events or update overlays.
 */
public class BotUtils {
    public static <T> void publishEvent(T event) {
        EventBus.publish(event);
        com.osrsbot.gui.OverlayManager.pushEvent(event.toString());
    }

    public static void overlayInfo(String info) {
        com.osrsbot.gui.OverlayManager.showInfo(info);
    }

    public static void notify(String msg) {
        com.osrsbot.gui.OverlayManager.notify(msg);
    }
}