package com.osrsbot.events.events;

import java.util.List;

/**
 * Fired when the player's inventory changes.
 */
public record InventoryChangedEvent(List<String> itemNames) {}