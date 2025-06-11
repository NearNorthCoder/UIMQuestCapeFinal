package com.osrsbot.hooks;

import com.osrsbot.debug.DebugManager;

import java.lang.reflect.Method;

/**
 * Reflection utility for accessing RuneLite's net.runelite.api.Client instance and methods.
 */
public class ClientReflection {
    private static volatile Object clientInstance = null;

    public static void setClientInstance(Object client) {
        clientInstance = client;
    }

    public static Object getClientInstance() {
        return clientInstance;
    }

    public static Object getLocalPlayer() {
        if (clientInstance == null) return null;
        try {
            Method getLocalPlayer = clientInstance.getClass().getMethod("getLocalPlayer");
            return getLocalPlayer.invoke(clientInstance);
        } catch (Exception e) {
            DebugManager.logException(e);
            return null;
        }
    }

    public static String getLocalPlayerName() {
        Object player = getLocalPlayer();
        if (player == null) return null;
        try {
            Method getName = player.getClass().getMethod("getName");
            Object name = getName.invoke(player);
            return name != null ? name.toString() : null;
        } catch (Exception e) {
            DebugManager.logException(e);
            return null;
        }
    }

    public static Object getLocalPlayerWorldLocation() {
        Object player = getLocalPlayer();
        if (player == null) return null;
        try {
            Method getWorldLocation = player.getClass().getMethod("getWorldLocation");
            return getWorldLocation.invoke(player);
        } catch (Exception e) {
            DebugManager.logException(e);
            return null;
        }
    }

    public static Integer getLocalPlayerX() {
        Object worldLocation = getLocalPlayerWorldLocation();
        if (worldLocation == null) return null;
        try {
            Method getX = worldLocation.getClass().getMethod("getX");
            Object x = getX.invoke(worldLocation);
            return x instanceof Integer ? (Integer)x : null;
        } catch (Exception e) {
            DebugManager.logException(e);
            return null;
        }
    }

    public static Integer getLocalPlayerY() {
        Object worldLocation = getLocalPlayerWorldLocation();
        if (worldLocation == null) return null;
        try {
            Method getY = worldLocation.getClass().getMethod("getY");
            Object y = getY.invoke(worldLocation);
            return y instanceof Integer ? (Integer)y : null;
        } catch (Exception e) {
            DebugManager.logException(e);
            return null;
        }
    }

    public static Integer getLocalPlayerHealthRatio() {
        Object player = getLocalPlayer();
        if (player == null) return null;
        try {
            Method getHealthRatio = player.getClass().getMethod("getHealthRatio");
            Object ratio = getHealthRatio.invoke(player);
            return ratio instanceof Integer ? (Integer)ratio : null;
        } catch (Exception e) {
            DebugManager.logException(e);
            return null;
        }
    }

    // Returns the client instance, so that advanced calls (like projecting world to minimap) are possible
    public static Object getClient() {
        return clientInstance;
    }
}