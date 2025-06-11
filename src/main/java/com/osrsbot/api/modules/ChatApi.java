package com.osrsbot.api.modules;

import com.osrsbot.debug.DebugManager;

/**
 * Chat API for reading and sending messages.
 */
public class ChatApi {
    public void sendPublicMessage(String message) {
        DebugManager.logApiCall("ChatApi.sendPublicMessage(" + message + ")");
        com.osrsbot.hooks.ClientReflection.sendPublicMessage(message);
    }

    public void sendPrivateMessage(String player, String message) {
        DebugManager.logApiCall("ChatApi.sendPrivateMessage(" + player + ", " + message + ")");
        // For PM, one would open the chatbox, type player's name, and send message. Left as an exercise.
        sendPublicMessage("/msg " + player + " " + message);
    }

    public String[] getLatestMessages(int count) {
        DebugManager.logApiCall("ChatApi.getLatestMessages(" + count + ")");
        return com.osrsbot.hooks.ClientReflection.getLatestMessages(count);
    }
}