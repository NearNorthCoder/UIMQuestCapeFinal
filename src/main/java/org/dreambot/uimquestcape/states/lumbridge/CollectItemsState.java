package org.dreambot.uimquestcape.states.lumbridge;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.NavigationHelper;

/**
 * Collects random item spawns in Lumbridge Castle
 */
public class CollectItemsState extends AbstractState {

    private static final Area LUMBRIDGE_CASTLE_AREA = new Area(
            new Tile(3201, 3228, 0),
            new Tile(3226, 3204, 0)
    );

    // Areas for pot and jug spawns
    private static final Area POT_AREA = new Area(
            new Tile(3208, 3214, 0),
            new Tile(3212, 3212, 0)
    );

    private static final Area JUG_AREA = new Area(
            new Tile(3211, 3214, 0),
            new Tile(3217, 3210, 0)
    );

    private final NavigationHelper navigation;
    private boolean collectedPot = false;
    private boolean collectedJug = false;

    public CollectItemsState(UIMQuestCape script) {
        super(script, "CollectItemsState");
        this.navigation = new NavigationHelper(script);
    }

    @Override
    public int execute() {
        // If we already collected everything, complete the state
        if (hasCollectedItems()) {
            Logger.log("All necessary items collected");
            complete();
            return 600;
        }

        // If we're not in Lumbridge Castle area, walk there
        if (!LUMBRIDGE_CASTLE_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to Lumbridge Castle");
            Walking.walk(LUMBRIDGE_CASTLE_AREA.getCenter());
            return 1000;
        }

        // Collect pot if we don't have one
        if (!collectedPot && !Inventory.contains("Pot")) {
            return collectPot();
        }

        // Collect jug if we don't have one
        if (!collectedJug && !Inventory.contains("Jug")) {
            return collectJug();
        }

        // Check if we got everything
        if (hasCollectedItems()) {
            Logger.log("All necessary items collected");
            complete();
        }

        return 600;
    }

    private int collectPot() {
        // If we're not at the pot area, walk there
        if (!POT_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to pot spawn");
            Walking.walk(POT_AREA.getCenter());
            return 600;
        }

        // Look for a pot on the ground or as a spawn
        GameObject potSpawn = GameObjects.closest(obj ->
                obj != null &&
                        obj.getName() != null &&
                        obj.getName().contains("Pot") &&
                        obj.hasAction("Take")
        );

        if (potSpawn != null) {
            Logger.log("Taking pot");
            potSpawn.interact("Take");
            Sleep.sleepUntil(() -> Inventory.contains("Pot"), 5000);
            collectedPot = Inventory.contains("Pot");
        } else {
            Logger.log("Pot not found, waiting for respawn");
            Sleep.sleep(1000, 2000);
        }

        return 600;
    }

    private int collectJug() {
        // If we're not at the jug area, walk there
        if (!JUG_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to jug spawn");
            Walking.walk(JUG_AREA.getCenter());
            return 600;
        }

        // Look for a jug on the ground or as a spawn
        GameObject jugSpawn = GameObjects.closest(obj ->
                obj != null &&
                        obj.getName() != null &&
                        obj.getName().contains("Jug") &&
                        obj.hasAction("Take")
        );

        if (jugSpawn != null) {
            Logger.log("Taking jug");
            jugSpawn.interact("Take");
            Sleep.sleepUntil(() -> Inventory.contains("Jug"), 5000);
            collectedJug = Inventory.contains("Jug");
        } else {
            Logger.log("Jug not found, waiting for respawn");
            Sleep.sleep(1000, 2000);
        }

        return 600;
    }

    private boolean hasCollectedItems() {
        // Check if we have a pot and jug in inventory
        boolean hasPot = Inventory.contains("Pot");
        boolean hasJug = Inventory.contains("Jug");

        return hasPot && hasJug;
    }

    @Override
    public boolean canExecute() {
        return !hasCollectedItems();
    }
}