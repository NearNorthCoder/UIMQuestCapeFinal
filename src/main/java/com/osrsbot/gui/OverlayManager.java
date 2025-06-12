package com.osrsbot.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Real in-game (Swing) overlay for displaying bot info, notifications, and recent events.
 */
public class OverlayManager {
    private static String lastChat = "";
    private static List<String> inventorySnapshot = Collections.emptyList();
    private static String lastCriticalError = null;
    private static final LinkedList<String> recentEvents = new LinkedList<>();
    private static final int MAX_EVENTS = 5;
    private static String notification = null;
    private static long notificationUntil = 0;

    // Swing Overlay
    private static JFrame overlayFrame = null;
    private static JTextArea overlayText = null;
    private static volatile String lastOverlayContent = "";

    static {
        SwingUtilities.invokeLater(OverlayManager::initOverlayWindow);
    }

    private static void initOverlayWindow() {
        overlayFrame = new JFrame("OSRSBot Overlay");
        overlayFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        overlayFrame.setAlwaysOnTop(true);
        overlayFrame.setSize(400, 300);
        overlayFrame.setLocationRelativeTo(null);
        overlayFrame.setResizable(true);
        overlayText = new JTextArea();
        overlayText.setEditable(false);
        overlayText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        overlayFrame.getContentPane().add(new JScrollPane(overlayText));
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
            SwingUtilities.invokeLater(() -> overlayText.setText(info));
        }
    }

    public static void setLastChat(String chat) {
        lastChat = chat;
    }

    public static void setInventorySnapshot(List<String> inv) {
        inventorySnapshot = inv;
    }

    public static void setLastCriticalError(String err) {
        lastCriticalError = err;
    }

    // --- REQUIRED STATIC METHODS FOR OTHER COMPONENTS ---

    public static void pushEvent(String event) {
        if (recentEvents.size() == MAX_EVENTS) recentEvents.removeFirst();
        recentEvents.addLast(event);
    }

    /**
     * Show a notification message in the overlay for a few seconds.
     */
    public static void notify(String msg) {
        notification = msg;
        notificationUntil = System.currentTimeMillis() + 5000;
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
}