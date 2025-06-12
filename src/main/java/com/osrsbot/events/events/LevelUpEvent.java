package com.osrsbot.events.events;

/**
 * Fired when the player gains a level in a skill.
 */
public record LevelUpEvent(String skill, int newLevel) {}