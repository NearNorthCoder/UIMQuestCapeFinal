package com.osrsbot.gui;

/**
 * Placeholder for a GUI overlay manager.
 * In a real implementation, this would use RuneLite's overlay system or AWT/Swing to display in-client info.
 */
public class OverlayManager {
    private static String lastChat = "";
    private static java.util.List<String> inventorySnapshot = java.util.Collections.emptyList();
    private static String lastCriticalError = null;
    private static final java.util.LinkedList<String> recentEvents = new java.util.LinkedList<>();
    private static final int MAX_EVENTS = 5;
    private static String notification = null;
    private static long notificationUntil = 0;

    // --- Swing Overlay ---
    private static javax.swing.JFrame overlayFrame = null;
    private static javax.swing.JTextArea overlayText = null;
    private static volatile String lastOverlayContent = "";

    static {
        try {
            javax.swing.SwingUtilities.invokeLater(OverlayManager::initOverlayWindow);
        } catch (Exception ignored) {}
    }

    private static void initOverlayWindow() {
        overlayFrame = new javax.swing.JFrame("OSRSBot Overlay");
        overlayFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        overlayFrame.setAlwaysOnTop(true);
        overlayFrame.setSize(400, 300);
        overlayFrame.setLocationRelativeTo(null);
        overlayFrame.setResizable(true);
        overlayText = new javax.swing.JTextArea();
        overlayText.setEditable(false);
        overlayText.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        overlayFrame.getContentPane().add(new javax.swing.JScrollPane(overlayText));
        overlayFrame.setVisible(true);
    }

    public static void showInfo(String info) {
        // Print to console and update overlay window
        System.out.println("[OVERLAY] " + info);
        updateOverlayWindow(info);
    }

    private static void updateOverlayWindow(String info) {
        lastOverlayContent = info;
        if (overlayText != null) {
            javax.swing.SwingUtilities.invokeLater(() -> overlayText.setText(info));
        }
    }

    public static void setLastChat(String chat) {
        lastChat = chat;
    }

    public static void setInventorySnapshot(java.util.List<String> inv) {
        inventorySnapshot = inv;
    }

    public static void updateOverlay() {
        StringBuilder sb = new StringBuilder();
        if (notification != null && System.currentTimeMillis() < notificationUntil) {
            sb.append("** ").append(notification).append(" **\n");
        }
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
        if (lastCriticalError != null) {
            sb.append("\nLast Critical Error: ").append(lastCriticalError);
        }
        if (!recentEvents.isEmpty()) {
            sb.append("\nRecent Events: ");
            for (String ev : recentEvents) {
                sb.append("\n  ").append(ev);
            }
        }
        showInfo(sb.toString());
    }