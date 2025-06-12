package com.osrsbot.events.events;

/**
 * Fired when a new chat message appears in the public chat.
 */
public record ChatMessageEvent(String sender, String message, String type) {}