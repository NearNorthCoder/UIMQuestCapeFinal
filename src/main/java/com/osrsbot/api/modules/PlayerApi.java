package com.osrsbot.api.modules;

import com.osrsbot.debug.DebugManager;

/**
 * Player API for reading and controlling the local player.
 */
public class PlayerApi {
    public String getName() {
        DebugManager.logApiCall("PlayerApi.getName");
        // TODO: Hook RuneLite's client.player.getName()
        return null;
    }

    public int getX() {
        DebugManager.logApiCall("PlayerApi.getX");
        // TODO: Hook player X position
        return -1;
    }

    public int getY() {
        DebugManager.logApiCall("PlayerApi.getY");
        // TODO: Hook player Y position
        return -1;
    }

    public int getHealth() {
        DebugManager.logApiCall("PlayerApi.getHealth");
        // TODO: Hook player health
        return -1;
    }

    public void walkTo(int x, int y) {
        DebugManager.logApiCall("PlayerApi.walkTo(" + x + ", " + y + ")");
        // TODO: Pathfinding and simulate click/movement
    }
}