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

    // --- INVENTORY HOOKS ---
    public static Object getInventoryContainer() {
        try {
            Object client = getClient();
            if (client == null) return null;
            // InventoryID.INVENTORY = 93, or use InventoryID enum if available
            Class<?> inventoryIdClass = client.getClass().getClassLoader().loadClass("net.runelite.api.InventoryID");
            Object inventoryId = null;
            for (Object constant : inventoryIdClass.getEnumConstants()) {
                if (constant.toString().equals("INVENTORY")) {
                    inventoryId = constant;
                    break;
                }
            }
            if (inventoryId == null) return null;
            java.lang.reflect.Method getItemContainer = client.getClass().getMethod("getItemContainer", inventoryIdClass);
            Object container = getItemContainer.invoke(client, inventoryId);
            return container;
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return null;
        }
    }

    public static Object[] getInventoryItems() {
        try {
            Object container = getInventoryContainer();
            if (container == null) return null;
            java.lang.reflect.Method getItems = container.getClass().getMethod("getItems");
            Object items = getItems.invoke(container);
            if (items instanceof Object[]) {
                return (Object[]) items;
            }
            return null;
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return null;
        }
    }

    // --- GAME STATE HOOKS ---
    public static String getGameState() {
        try {
            Object client = getClient();
            if (client == null) return null;
            java.lang.reflect.Method getGameState = client.getClass().getMethod("getGameState");
            Object state = getGameState.invoke(client);
            return state != null ? state.toString() : null;
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return null;
        }
    }

    public static Integer getFps() {
        try {
            Object client = getClient();
            if (client == null) return null;
            java.lang.reflect.Method getFps = client.getClass().getMethod("getFPS");
            Object fps = getFps.invoke(client);
            return fps instanceof Integer ? (Integer) fps : null;
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return null;
        }
    }

    public static Integer getTickCount() {
        try {
            Object client = getClient();
            if (client == null) return null;
            java.lang.reflect.Method getTickCount = client.getClass().getMethod("getTickCount");
            Object ticks = getTickCount.invoke(client);
            return ticks instanceof Integer ? (Integer) ticks : null;
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return null;
        }
    }

    // --- WORLD HOOKS ---
    public static Object getWorldService() {
        try {
            Object client = getClient();
            if (client == null) return null;
            // In RuneLite, world list is managed by WorldService; for direct world access, try getWorlds()
            java.lang.reflect.Method getWorlds = client.getClass().getMethod("getWorlds");
            return getWorlds.invoke(client);
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return null;
        }
    }

    public static Object[] getWorlds() {
        try {
            Object worlds = getWorldService();
            if (worlds == null) return null;
            // Worlds is a net.runelite.api.WorldResult
            java.lang.reflect.Method getWorlds = worlds.getClass().getMethod("getWorlds");
            Object list = getWorlds.invoke(worlds);
            if (list instanceof java.util.List) {
                java.util.List<?> wl = (java.util.List<?>) list;
                return wl.toArray();
            }
            return null;
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return null;
        }
    }

    public static boolean hopToWorld(int worldId) {
        try {
            Object client = getClient();
            if (client == null) return false;
            java.lang.reflect.Method changeWorld = client.getClass().getMethod("changeWorld", int.class);
            changeWorld.invoke(client, worldId);
            return true;
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return false;
        }
    }

    // --- WIDGET HOOKS ---
    public static Object getWidget(int widgetId) {
        try {
            Object client = getClient();
            if (client == null) return null;
            java.lang.reflect.Method getWidget = client.getClass().getMethod("getWidget", int.class);
            return getWidget.invoke(client, widgetId);
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return null;
        }
    }

    public static boolean isWidgetVisible(int widgetId) {
        Object widget = getWidget(widgetId);
        if (widget == null) return false;
        try {
            java.lang.reflect.Method isHidden = widget.getClass().getMethod("isHidden");
            Object hidden = isHidden.invoke(widget);
            return (hidden instanceof Boolean) ? !((Boolean) hidden) : false;
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return false;
        }
    }

    public static boolean clickWidget(int widgetId) {
        // Widget actions are typically performed by simulating input or invoking menu actions
        // For demo, we'll get the widget location and click via InputApi
        Object widget = getWidget(widgetId);
        if (widget == null) return false;
        try {
            java.lang.reflect.Method getBounds = widget.getClass().getMethod("getBounds");
            Object bounds = getBounds.invoke(widget);
            if (bounds != null) {
                java.lang.reflect.Method getX = bounds.getClass().getMethod("getX");
                java.lang.reflect.Method getY = bounds.getClass().getMethod("getY");
                java.lang.reflect.Method getWidth = bounds.getClass().getMethod("getWidth");
                java.lang.reflect.Method getHeight = bounds.getClass().getMethod("getHeight");
                int x = ((Number) getX.invoke(bounds)).intValue();
                int y = ((Number) getY.invoke(bounds)).intValue();
                int w = ((Number) getWidth.invoke(bounds)).intValue();
                int h = ((Number) getHeight.invoke(bounds)).intValue();
                // Click center of widget
                com.osrsbot.api.ApiManager.get().input.click(x + w / 2, y + h / 2);
                return true;
            }
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
        }
        return false;
    }

    // --- CHAT HOOKS ---
    public static boolean sendPublicMessage(String message) {
        try {
            Object client = getClient();
            if (client == null) return false;
            java.lang.reflect.Method invokeMenuAction = client.getClass().getMethod("invokeMenuAction", String.class, String.class, int.class, int.class, int.class, int.class);
            // For chat, action is 0 (typing), slot etc. are set to 0
            invokeMenuAction.invoke(client, message, "", 0, 0, 0, 0);
            return true;
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return false;
        }
    }

    public static String[] getLatestMessages(int count) {
        try {
            Object client = getClient();
            if (client == null) return new String[0];
            java.lang.reflect.Method getMessages = client.getClass().getMethod("getMessages");
            Object messagesObj = getMessages.invoke(client);
            if (messagesObj == null) return new String[0];
            java.util.List<?> messages = (java.util.List<?>) messagesObj;
            int n = Math.min(count, messages.size());
            String[] result = new String[n];
            for (int i = 0; i < n; i++) {
                Object chatMsg = messages.get(messages.size() - 1 - i);
                java.lang.reflect.Method getMsg = chatMsg.getClass().getMethod("getMessage");
                Object msgText = getMsg.invoke(chatMsg);
                result[i] = msgText != null ? msgText.toString() : "";
            }
            return result;
        } catch (Exception e) {
            com.osrsbot.debug.DebugManager.logException(e);
            return new String[0];
        }
    }
}