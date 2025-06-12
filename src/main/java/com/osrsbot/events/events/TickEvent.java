package com.osrsbot.events.events;

/**
 * Event fired every game tick (could be called from tick polling or hook).
 */
public record TickEvent(long timeMillis) {}