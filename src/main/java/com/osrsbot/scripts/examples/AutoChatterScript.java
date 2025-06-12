package com.osrsbot.scripts.examples;

import com.osrsbot.scripts.Script;
import com.osrsbot.api.ApiManager;
import com.osrsbot.debug.DebugManager;

import java.util.Random;

/**
 * Example script: says something in public chat every 30-60 seconds.
 */
public class AutoChatterScript implements Script {
    private volatile boolean running = false;
    private final Random rng = new Random();

    @Override
    public String getName() {
        return "AutoChatterScript";
    }

    @Override
    public void onStart() {
        running = true;
        DebugManager.logInfo("AutoChatterScript started.");
    }

    @Override
    public void onStop() {
        running = false;
        DebugManager.logInfo("AutoChatterScript stopped.");
    }

    @Override
    public void run() {
        while (running) {
            try {
                String[] messages = {
                    "Hello Gielinor!",
                   // "Automated message from OSRSBot.",
                    "Just grinding some skills.",
                    "GL HF everyone!",
                    //"Bots? Where?!"
                };
                String msg = messages[rng.nextInt(messages.length)];
                ApiManager.get().chat.sendPublicMessage(msg);
                DebugManager.logInfo("AutoChatterScript: Sent message '" + msg + "'");
                Thread.sleep(30000 + rng.nextInt(30000));
            } catch (InterruptedException e) {
                break;
            } catch (Exception ex) {
                DebugManager.logException(ex);
            }
        }
    }
}