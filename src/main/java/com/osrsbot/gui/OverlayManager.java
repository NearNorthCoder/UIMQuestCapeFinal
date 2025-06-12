package com.osrsbot.gui;

/**
 * Placeholder for a GUI overlay manager.
 * In a real implementation, this would use RuneLite's overlay system or AWT/Swing to display in-client info.
 */
public class OverlayManager {
    public static void showInfo(String info) {
        // For now, print to console. In a real overlay, render in-client.
        System.out.println("[OVERLAY] " + info);
    }

    public static void updateOverlay() {
        StringBuilder sb = new StringBuilder();
        sb.append("Active Scripts: ");
        var scripts = com.osrsbot.scripts.ScriptManager.getScripts();
        boolean any = false;
        for (var script : scripts) {
            sb.append(script.getName()).append(" ");
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
        showInfo(sb.toString());
    }
}