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
}