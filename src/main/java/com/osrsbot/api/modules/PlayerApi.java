package com.osrsbot.api.modules;

import com.osrsbot.debug.DebugManager;
import com.osrsbot.hooks.ClientReflection;

/**
 * Player API for reading and controlling the local player.
 */
public class PlayerApi {
    public String getName() {
        DebugManager.logApiCall("PlayerApi.getName");
        String name = ClientReflection.getLocalPlayerName();
        if (name == null) {
            DebugManager.logWarn("Could not retrieve player name.");
        }
        return name;
    }

    public int getX() {
        DebugManager.logApiCall("PlayerApi.getX");
        Integer x = com.osrsbot.hooks.ClientReflection.getLocalPlayerX();
        if (x == null) {
            DebugManager.logWarn("Could not retrieve player X position.");
            return -1;
        }
        return x;
    }

    public int getY() {
        DebugManager.logApiCall("PlayerApi.getY");
        Integer y = com.osrsbot.hooks.ClientReflection.getLocalPlayerY();
        if (y == null) {
            DebugManager.logWarn("Could not retrieve player Y position.");
            return -1;
        }
        return y;
    }

    public int getHealth() {
        DebugManager.logApiCall("PlayerApi.getHealth");
        Integer health = com.osrsbot.hooks.ClientReflection.getLocalPlayerHealthRatio();
        if (health == null) {
            DebugManager.logWarn("Could not retrieve player health ratio.");
            return -1;
        }
        return health;
    }

    public void walkTo(int x, int y) {
        DebugManager.logApiCall("PlayerApi.walkTo(" + x + ", " + y + ")");
        // To walk to a tile, click its projected location on the minimap.
        try {
            Object client = com.osrsbot.hooks.ClientReflection.getClient();
            if (client == null) {
                DebugManager.logWarn("Client instance not available for walkTo.");
                return;
            }
            // getMinimapImage() and worldToMinimap() are available in RuneLite's API.
            // We'll project world coordinates to minimap screen coordinates.
            // This requires world to minimap projection:
            java.lang.reflect.Method worldToMinimap = client.getClass().getMethod("worldToMinimap", int.class, int.class);
            Object point = worldToMinimap.invoke(client, x, y);

            if (point != null) {
                java.awt.Point pt = null;
                if (point instanceof java.awt.Point) {
                    pt = (java.awt.Point) point;
                } else {
                    // If not java.awt.Point, try reflection
                    java.lang.reflect.Method getX = point.getClass().getMethod("getX");
                    java.lang.reflect.Method getY = point.getClass().getMethod("getY");
                    int px = ((Number) getX.invoke(point)).intValue();
                    int py = ((Number) getY.invoke(point)).intValue();
                    pt = new java.awt.Point(px, py);
                }
                if (pt != null) {
                    DebugManager.logInfo("Simulating click on minimap at (" + pt.x + "," + pt.y + ")");
                    com.osrsbot.api.ApiManager.get().input.click(pt.x, pt.y);
                } else {
                    DebugManager.logWarn("worldToMinimap projection returned null point for ("+x+","+y+").");
                }
            } else {
                DebugManager.logWarn("Could not project world coordinates to minimap for ("+x+","+y+").");
            }
        } catch (Exception e) {
            DebugManager.logException(e);
        }
    }
}