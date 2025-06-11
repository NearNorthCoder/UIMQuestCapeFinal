package com.osrsbot.api.modules;

import com.osrsbot.debug.DebugManager;

/**
 * Chat API for reading and sending messages.
 */
public class ChatApi {
    public void sendPublicMessage(String message) {
        DebugManager.logApiCall("ChatApi.sendPublicMessage(" + message + ")");
        // TODO: Simulate chat message input
    }

    public void sendPrivateMessage(String player, String message) {
        DebugManager.logApiCall("ChatApi.sendPrivateMessage(" + player + ", " + message + ")");
        // TODO: Simulate PM
    }

    public String[] getLatestMessages(int count) {
        DebugManager.logApiCall("ChatApi.getLatestMessages(" + count + ")");
        // TODO: Read chat history from client
        return new String[0];
    }
}