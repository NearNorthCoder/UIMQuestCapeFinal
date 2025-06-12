package com.osrsbot.antiban;

import com.osrsbot.api.ApiManager;
import com.osrsbot.debug.DebugManager;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Advanced antiban manager—performs randomized humanlike actions in the background.
 */
public class AntibanManager implements com.osrsbot.modules.BotModule {
    private final Random rng = new Random();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;

    @Override
    public String getName() {
        return "AntibanManager";
    }

    @Override
    public void onStart() {
        running = true;
        executor.submit(this::antibanLoop);
        DebugManager.logInfo("AntibanManager started.");
    }

    @Override
    public void onStop() {
        running = false;
        executor.shutdownNow();
        DebugManager.logInfo("AntibanManager stopped.");
    }

    private void antibanLoop() {
        while (running) {
            try {
                int action = rng.nextInt(4);
                switch (action) {
                    case 0 -> moveMouseRandomly();
                    case 1 -> idleAFK();
                    case 2 -> randomCameraMove();
                    case 3 -> randomBreak();
                }
                Thread.sleep(1000 + rng.nextInt(4000));
            } catch (InterruptedException e) {
                break;
            } catch (Exception ex) {
                DebugManager.logException(ex);
            }
        }
    }

    private void moveMouseRandomly() {
        int screenW = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        int x = rng.nextInt(screenW);
        int y = rng.nextInt(screenH);
        DebugManager.logDebug("Antiban: Moving mouse randomly to (" + x + "," + y + ")");
        ApiManager.get().input.moveMouse(x, y);
    }

    private void idleAFK() throws InterruptedException {
        int ms = 800 + rng.nextInt(2200);
        DebugManager.logDebug("Antiban: Idling/AFKing for " + ms + "ms");
        Thread.sleep(ms);
    }

    private void randomCameraMove() {
        // Simulate camera move by holding keys (e.g. left/right/up/down arrows)
        String[] keys = {"LEFT", "RIGHT", "UP", "DOWN"};
        String key = keys[rng.nextInt(keys.length)];
        DebugManager.logDebug("Antiban: Simulating camera move (" + key + ")");
        // For demo, type key (in real use, send actual key event for arrow keys)
        // ApiManager.get().input.type(key); // Not realistic; would need native keycode
        // Example: press and release left arrow
        try {
            java.awt.Robot robot = new java.awt.Robot();
            int keyCode = switch (key) {
                case "LEFT" -> java.awt.event.KeyEvent.VK_LEFT;
                case "RIGHT" -> java.awt.event.KeyEvent.VK_RIGHT;
                case "UP" -> java.awt.event.KeyEvent.VK_UP;
                case "DOWN" -> java.awt.event.KeyEvent.VK_DOWN;
                default -> -1;
            };
            if (keyCode != -1) {
                robot.keyPress(keyCode);
                Thread.sleep(100 + rng.nextInt(200));
                robot.keyRelease(keyCode);
            }
        } catch (Exception e) {
            DebugManager.logException(e);
        }
    }

    private void randomBreak() throws InterruptedException {
        if (rng.nextDouble() < 0.1) {
            int ms = 10000 + rng.nextInt(15000);
            DebugManager.logInfo("Antiban: Taking a long randomized break for " + ms + "ms");
            Thread.sleep(ms);
        }
    }
}