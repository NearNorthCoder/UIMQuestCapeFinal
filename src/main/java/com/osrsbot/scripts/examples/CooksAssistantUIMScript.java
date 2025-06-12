package com.osrsbot.scripts.examples;

import com.osrsbot.scripts.Script;
import com.osrsbot.api.ApiManager;
import com.osrsbot.debug.DebugManager;
import com.osrsbot.util.BotUtils;

import java.util.*;

public class CooksAssistantUIMScript implements Script {
    private volatile boolean running = false;

    // Quest states
    private enum State {
        WALK_TO_LUMBRIDGE,
        START_QUEST,
        COLLECT_BUCKET,
        FILL_BUCKET_MILK,
        COLLECT_POT,
        FILL_POT_FLOUR,
        COLLECT_EGG,
        RETURN_TO_COOK,
        COMPLETE_QUEST,
        DONE
    }
    private State state = State.WALK_TO_LUMBRIDGE;

    // Tile coordinates (approximate, world space)
    private static final int[] LUMBRIDGE_COOKS_ROOM = {3207, 3213};
    private static final int[] DAIRY_COW_TILE = {3256, 3297};
    private static final int[] MILK_TILE = {3253, 3295};
    private static final int[] CHICKEN_EGG_TILE = {3236, 3295};
    private static final int[] POT_SPAWN_TILE = {3190, 3272};
    private static final int[] FLOUR_BIN_TILE = {3166, 3306};
    private static final int[] WHEAT_FIELD_TILE = {3160, 3292};
    private static final int[] WINDMILL_LADDER_TILE = {3166, 3305};

    private final Random rng = new Random();

    @Override
    public String getName() {
        return "CooksAssistantUIMScript";
    }

    @Override
    public void onStart() {
        running = true;
        state = State.WALK_TO_LUMBRIDGE;
        BotUtils.notify("Starting Cook's Assistant for UIM!");
        DebugManager.logInfo("CooksAssistantUIMScript started.");
    }

    @Override
    public void onStop() {
        running = false;
        BotUtils.notify("CooksAssistantUIMScript stopped.");
        DebugManager.logInfo("CooksAssistantUIMScript stopped.");
    }

    @Override
    public void run() {
        while (running && state != State.DONE) {
            try {
                switch (state) {
                    case WALK_TO_LUMBRIDGE -> walkToLumbridge();
                    case START_QUEST -> startQuest();
                    case COLLECT_BUCKET -> collectBucket();
                    case FILL_BUCKET_MILK -> fillBucketWithMilk();
                    case COLLECT_POT -> collectPot();
                    case FILL_POT_FLOUR -> fillPotWithFlour();
                    case COLLECT_EGG -> collectEgg();
                    case RETURN_TO_COOK -> returnToCook();
                    case COMPLETE_QUEST -> completeQuest();
                }
                Thread.sleep(2000 + rng.nextInt(1000));
            } catch (InterruptedException e) {
                break;
            } catch (Exception ex) {
                DebugManager.logException(ex);
                BotUtils.notify("Error: " + ex.getMessage());
                break;
            }
        }
        BotUtils.notify("Cook's Assistant script finished.");
        DebugManager.logInfo("CooksAssistantUIMScript finished.");
    }

    private void walkToLumbridge() throws InterruptedException {
        if (near(LUMBRIDGE_COOKS_ROOM)) {
            state = State.START_QUEST;
            return;
        }
        DebugManager.logInfo("Walking to Lumbridge Castle kitchen.");
        ApiManager.get().player.walkTo(LUMBRIDGE_COOKS_ROOM[0], LUMBRIDGE_COOKS_ROOM[1]);
        Thread.sleep(5000);
    }

    private void startQuest() throws InterruptedException {
        // Interact with the cook to start the quest
        DebugManager.logInfo("Starting the quest by talking to the Cook.");
        BotUtils.notify("Talking to Cook to start...");
        interactWithNpc("Cook");
        Thread.sleep(5000);
        state = State.COLLECT_BUCKET;
    }

    private void collectBucket() throws InterruptedException {
        if (hasItem("Bucket")) {
            state = State.FILL_BUCKET_MILK;
            return;
        }
        DebugManager.logInfo("Collecting a bucket.");
        // There's a bucket spawn in the kitchen
        ApiManager.get().player.walkTo(LUMBRIDGE_COOKS_ROOM[0] + 1, LUMBRIDGE_COOKS_ROOM[1]);
        Thread.sleep(2000);
        interactWithGroundItem("Bucket");
        Thread.sleep(2000);
    }

    private void fillBucketWithMilk() throws InterruptedException {
        if (hasItem("Bucket of milk")) {
            state = State.COLLECT_POT;
            return;
        }
        if (!hasItem("Bucket")) {
            state = State.COLLECT_BUCKET;
            return;
        }
        DebugManager.logInfo("Walking to dairy cow for milk.");
        ApiManager.get().player.walkTo(DAIRY_COW_TILE[0], DAIRY_COW_TILE[1]);
        Thread.sleep(7000);
        interactWithNpc("Dairy cow"); // Use bucket on dairy cow
        Thread.sleep(4000);
    }

    private void collectPot() throws InterruptedException {
        if (hasItem("Pot")) {
            state = State.FILL_POT_FLOUR;
            return;
        }
        DebugManager.logInfo("Collecting a pot.");
        ApiManager.get().player.walkTo(POT_SPAWN_TILE[0], POT_SPAWN_TILE[1]);
        Thread.sleep(4000);
        interactWithGroundItem("Pot");
        Thread.sleep(2000);
    }

    private void fillPotWithFlour() throws InterruptedException {
        if (hasItem("Pot of flour")) {
            state = State.COLLECT_EGG;
            return;
        }
        if (!hasItem("Pot")) {
            state = State.COLLECT_POT;
            return;
        }
        DebugManager.logInfo("Getting grain, milling it, and collecting flour (UIM-friendly).");
        // Get wheat from the field
        ApiManager.get().player.walkTo(WHEAT_FIELD_TILE[0], WHEAT_FIELD_TILE[1]);
        Thread.sleep(5000);
        interactWithGroundItem("Wheat");
        Thread.sleep(2000);

        // Go to windmill, climb to top, use grain on hopper, operate hopper, climb down, collect flour
        ApiManager.get().player.walkTo(WINDMILL_LADDER_TILE[0], WINDMILL_LADDER_TILE[1]);
        Thread.sleep(3000);
        interactWithObject("Ladder"); // Climb up
        Thread.sleep(2000);
        interactWithObject("Hopper"); // Use grain on hopper
        Thread.sleep(2000);
        interactWithObject("Hopper controls"); // Operate hopper
        Thread.sleep(2000);
        interactWithObject("Ladder"); // Climb down
        Thread.sleep(2000);
        ApiManager.get().player.walkTo(FLOUR_BIN_TILE[0], FLOUR_BIN_TILE[1]);
        Thread.sleep(2000);
        interactWithObject("Flour bin");
        Thread.sleep(2000);
    }

    private void collectEgg() throws InterruptedException {
        if (hasItem("Egg")) {
            state = State.RETURN_TO_COOK;
            return;
        }
        DebugManager.logInfo("Collecting egg from chicken pen.");
        ApiManager.get().player.walkTo(CHICKEN_EGG_TILE[0], CHICKEN_EGG_TILE[1]);
        Thread.sleep(4000);
        interactWithGroundItem("Egg");
        Thread.sleep(2000);
    }

    private void returnToCook() throws InterruptedException {
        if (!hasItem("Pot of flour") || !hasItem("Bucket of milk") || !hasItem("Egg")) {
            DebugManager.logInfo("Missing quest items, re-gathering...");
            if (!hasItem("Pot of flour")) state = State.FILL_POT_FLOUR;
            else if (!hasItem("Bucket of milk")) state = State.FILL_BUCKET_MILK;
            else state = State.COLLECT_EGG;
            return;
        }
        DebugManager.logInfo("Returning to Cook with all items.");
        ApiManager.get().player.walkTo(LUMBRIDGE_COOKS_ROOM[0], LUMBRIDGE_COOKS_ROOM[1]);
        Thread.sleep(5000);
        state = State.COMPLETE_QUEST;
    }

    private void completeQuest() throws InterruptedException {
        DebugManager.logInfo("Completing quest by talking to the Cook.");
        BotUtils.notify("Turning in Cook's Assistant...");
        interactWithNpc("Cook");
        Thread.sleep(6000);
        state = State.DONE;
    }

    // --- Helper methods ---

    private boolean hasItem(String itemName) {
        return ApiManager.get().inventory.getInventory().stream()
                .anyMatch(item -> item.name.equalsIgnoreCase(itemName));
    }

    private boolean near(int[] tile) {
        int x = ApiManager.get().player.getX();
        int y = ApiManager.get().player.getY();
        return (Math.abs(x - tile[0]) < 4) && (Math.abs(y - tile[1]) < 4);
    }

    private void interactWithNpc(String npcName) throws InterruptedException {
        // In a real implementation, you'd use WorldApi/NPC hooks to find and click the NPC
        DebugManager.logInfo("Interacting with NPC: " + npcName);
        BotUtils.notify("Interacting with " + npcName);
        // For now, just simulate a click near the NPC tile
        Thread.sleep(1200 + rng.nextInt(800));
    }

    private void interactWithGroundItem(String itemName) throws InterruptedException {
        DebugManager.logInfo("Attempting to pick up ground item: " + itemName);
        BotUtils.notify("Getting " + itemName);
        Thread.sleep(1200 + rng.nextInt(800));
    }

    private void interactWithObject(String objectName) throws InterruptedException {
        DebugManager.logInfo("Interacting with object: " + objectName);
        BotUtils.notify("Using " + objectName);
        Thread.sleep(1200 + rng.nextInt(800));
    }
}