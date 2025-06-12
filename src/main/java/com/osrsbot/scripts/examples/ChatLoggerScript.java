package com.osrsbot.scripts.examples;

import com.osrsbot.scripts.Script;
import com.osrsbot.debug.DebugManager;
import com.osrsbot.events.EventBus;
import com.osrsbot.events.events.ChatMessageEvent;

/**
 * Script that logs all new public chat messages in real time.
 */
public class ChatLoggerScript implements Script {
    private volatile boolean running = false;
    private final java.util.function.Consumer<ChatMessageEvent> handler = event -> {
        DebugManager.logInfo("[ChatLogger] " + event.type() + ": " + event.sender() + ": " + event.message());
    };

    @Override
    public String getName() {
        return "ChatLoggerScript";
    }

    @Override
    public void onStart() {
        running = true;
        EventBus.subscribe(ChatMessageEvent.class, handler);
        DebugManager.logInfo("ChatLoggerScript started.");
    }

    @Override
    public void onStop() {
        running = false;
        EventBus.unsubscribe(ChatMessageEvent.class, handler);
        DebugManager.logInfo("ChatLoggerScript stopped.");
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}