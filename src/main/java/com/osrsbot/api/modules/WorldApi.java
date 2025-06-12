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
        java.util.ArrayList<WorldInfo> result = new java.util.ArrayList<>();
        Object[] worlds = com.osrsbot.hooks.ClientReflection.getWorlds();
        if (worlds == null) return result;
        try {
            for (Object w : worlds) {
                java.lang.reflect.Method getId = w.getClass().getMethod("getId");
                java.lang.reflect.Method getTypes = w.getClass().getMethod("getTypes");
                int id = ((Number) getId.invoke(w)).intValue();
                Object typeList = getTypes.invoke(w);
                String typeStr = (typeList != null) ? typeList.toString() : "";
                result.add(new WorldInfo(id, typeStr));
            }
        } catch (Exception e) {
            DebugManager.logException(e);
        }
        return result;
    }

    public void hopWorld(int worldId) {
        DebugManager.logApiCall("WorldApi.hopWorld(" + worldId + ")");
        com.osrsbot.hooks.ClientReflection.hopToWorld(worldId);
    }
}