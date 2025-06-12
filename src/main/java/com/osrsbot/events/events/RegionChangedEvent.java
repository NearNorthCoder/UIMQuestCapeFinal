package com.osrsbot.events.events;

/**
 * Fired when the player moves to a different region.
 */
public record RegionChangedEvent(int regionId) {}