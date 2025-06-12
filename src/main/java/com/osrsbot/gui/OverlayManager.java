package com.osrsbot.gui;

/**
 * Placeholder for a GUI overlay manager.
 * In a real implementation, this would use RuneLite's overlay system or AWT/Swing to display in-client info.
 */
public class OverlayManager {
    private static String lastChat = "";
    private static java.util.List<String> inventorySnapshot = java.util.Collections.emptyList();

    public static void showInfo(String info) {
        // For now, print to console. In a real overlay, render in-client.
        System.out.println("[OVERLAY] " + info);
    }

    public static void setLastChat(String chat) {
        lastChat = chat;
    }

    public static void setInventorySnapshot(java.util.List<String> inv) {
        inventorySnapshot = inv;
    }

    public static void updateOverlay() {
        StringBuilder sb = new StringBuilder();
        sb.append("Scripts: ");
        var scripts = com.osrsbot.scripts.ScriptManager.getScripts();
        boolean any = false;
        for (var script : scripts) {
            sb.append(script.getName())
                    .append('[')
                    .append(com.osrsbot.scripts.ScriptManager.getScriptState(script))
                    .append("] ");
            any = true;
        }
        if (!any) sb.append("(none) ");
        sb.append("| Modules: ");
        var mods = com.osrsbot.modules.ModuleManager.getModules();
        any = false;
        for (var mod : mods) {
            sb.append(mod.getName()).append(" ");
            any = true;
        }
        sb.append("\nLast Chat: ").append(lastChat);
        sb.append("\nInventory: ").append(inventorySnapshot);
        showInfo(sb.toString());
    }
}