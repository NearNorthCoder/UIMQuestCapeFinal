package com.osrsbot.events.events;

/**
 * Fired when an NPC appears near the player.
 */
public record NpcDetectedEvent(String npcName, int npcId, int x, int y) {}