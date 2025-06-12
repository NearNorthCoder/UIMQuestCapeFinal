package com.osrsbot.events.events;

/**
 * Fired when the XP of any skill changes.
 */
public record XpChangedEvent(String skill, int newXp) {}