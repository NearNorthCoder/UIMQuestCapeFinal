package com.osrsbot.scripts.examples;

import com.osrsbot.scripts.Script;
import com.osrsbot.events.EventBus;
import com.osrsbot.events.events.NpcDetectedEvent;
import com.osrsbot.util.BotUtils;

/**
 * Script that notifies the overlay when a Goblin or a Cow is detected nearby.
 */
public class NpcNotifierScript implements Script {
    private volatile boolean running = false;
    private final java.util.function.Consumer<NpcDetectedEvent> npcHandler = event -> {
        if (event.npcName() != null && (
                event.npcName().equalsIgnoreCase("Goblin") ||
                event.npcName().equalsIgnoreCase("Cow"))) {
            BotUtils.notify("Detected " + event.npcName() + " at [" + event.x() + "," + event.y() + "]");
        }
    };

    @Override
    public String getName() {
        return "NpcNotifierScript";
    }

    @Override
    public void onStart() {
        running = true;
        EventBus.subscribe(NpcDetectedEvent.class, npcHandler);
        BotUtils.notify("NpcNotifierScript started");
    }

    @Override
    public void onStop() {
        running = false;
        EventBus.unsubscribe(NpcDetectedEvent.class, npcHandler);
        BotUtils.notify("NpcNotifierScript stopped");
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