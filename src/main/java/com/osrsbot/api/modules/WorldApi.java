package com.osrsbot.api.modules;

import com.osrsbot.debug.DebugManager;
import java.util.List;

/**
 * World API for getting world info, NPCs, objects, etc.
 */
public class WorldApi {
    public static class WorldInfo {
        public final int id;
        public final String type;
        public WorldInfo(int id, String type) { this.id = id; this.type = type; }
    }

    public List<WorldInfo> getWorlds() {
        DebugManager.logApiCall("WorldApi.getWorlds");
        // TODO: Pull world list from client
        return List.of();
    }

    public void hopWorld(int worldId) {
        DebugManager.logApiCall("WorldApi.hopWorld(" + worldId + ")");
        // TODO: Simulate world hop
    }
}