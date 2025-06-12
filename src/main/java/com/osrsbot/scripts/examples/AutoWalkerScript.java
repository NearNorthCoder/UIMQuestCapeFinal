package com.osrsbot.scripts.examples;

import com.osrsbot.scripts.Script;
import com.osrsbot.api.ApiManager;
import com.osrsbot.debug.DebugManager;

/**
 * Example script: walks the player back and forth between two world locations every 30 seconds.
 */
public class AutoWalkerScript implements Script {
    private volatile boolean running = false;

    // Example coordinates: Lumbridge (3222, 3218) and Varrock (3210, 3424)
    private static final int[][] LOCATIONS = {
        {3222, 3218}, // Lumbridge
        {3210, 3424}  // Varrock
    };

    @Override
    public String getName() {
        return "AutoWalkerScript";
    }

    @Override
    public void onStart() {
        running = true;
        DebugManager.logInfo("AutoWalkerScript started.");
    }

    @Override
    public void onStop() {
        running = false;
        DebugManager.logInfo("AutoWalkerScript stopped.");
    }

    @Override
    public void run() {
        int target = 1;
        while (running) {
            try {
                int x = LOCATIONS[target][0];
                int y = LOCATIONS[target][1];
                DebugManager.logInfo("AutoWalkerScript: Walking to (" + x + ", " + y + ")");
                ApiManager.get().player.walkTo(x, y);
                target = 1 - target; // Switch destination
                Thread.sleep(30000); // Wait 30 seconds before next move
            } catch (InterruptedException e) {
                break;
            } catch (Exception ex) {
                DebugManager.logException(ex);
            }
        }
    }
}