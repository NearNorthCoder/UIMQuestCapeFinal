package com.osrsbot.util;

import com.osrsbot.events.EventBus;

public class BotUtils {
    /**
     * Publish an event to the global EventBus and add it to the overlay's recent events.
     */
    public static <T> void publishEvent(T event) {
        EventBus.publish(event);
        com.osrsbot.gui.OverlayManager.pushEvent(event.toString());
    }

    /**
     * Display info in the overlay window.
     */
    public static void overlayInfo(String info) {
        com.osrsbot.gui.OverlayManager.showInfo(info);
    }

    /**
     * Show a notification in the overlay window.
     */
    public static void notify(String msg) {
        com.osrsbot.gui.OverlayManager.notify(msg);
    }
}