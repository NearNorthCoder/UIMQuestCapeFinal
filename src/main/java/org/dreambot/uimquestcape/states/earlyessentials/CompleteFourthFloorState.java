package org.dreambot.uimquestcape.states.earlyessentials;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.QuestVarbitManager;

public class CompleteFourthFloorState extends AbstractState {

    private static final int STRONGHOLD_VARBIT = 853;
    private static final int FOURTH_FLOOR_START = 5;
    private static final int FOURTH_FLOOR_COMPLETE = 7;

    private static final Area FOURTH_FLOOR_AREA = new Area(1856, 5248, 1920, 5184, 3);

    public CompleteFourthFloorState(UIMQuestCape script) {
        super(script, "CompleteFourthFloorState");
    }

    @Override
    public int execute() {
        // Check if fourth floor is already completed
        if (QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) >= FOURTH_FLOOR_COMPLETE) {
            Logger.log("Fourth floor already completed");
            complete();
            return 600;
        }

        // Make sure we're on the fourth floor
        if (!FOURTH_FLOOR_AREA.contains(Players.getLocal())) {
            // Find and use stairs from third floor
            GameObject stairs = GameObjects.closest(obj ->
                    obj != null &&
                            obj.getName() != null &&
                            obj.getName().contains("Ladder") &&
                            obj.hasAction("Climb-down")
            );

            if (stairs != null) {
                Logger.log("Using stairs to reach fourth floor");
                stairs.interact("Climb-down");
                Sleep.sleepUntil(() -> FOURTH_FLOOR_AREA.contains(Players.getLocal()), 5000);
            }

            return 1000;
        }

        // Navigate to the final reward chest
        GameObject rewardChest = GameObjects.closest("Gift of peace");
        if (rewardChest != null) {
            Logger.log("Opening final reward chest");
            rewardChest.interact("Open");
            Sleep.sleepUntil(() ->
                            QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) >= FOURTH_FLOOR_COMPLETE ||
                                    Inventory.contains("Coins") && Inventory.count("Coins") >= 10000,
                    5000
            );

            if (QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) >= FOURTH_FLOOR_COMPLETE ||
                    (Inventory.contains("Coins") && Inventory.count("Coins") >= 10000)) {
                complete();
            }

            return 600;
        }

        // Navigate through corridors (simplified)
        Logger.log("Navigating through fourth floor");

        // Find the next door or portal to proceed
        GameObject nextDoor = GameObjects.closest(obj ->
                obj != null &&
                        obj.getName() != null &&
                        (obj.getName().contains("Door") || obj.getName().contains("Gate")) &&
                        obj.hasAction("Open")
        );

        if (nextDoor != null) {
            nextDoor.interact("Open");
            Sleep.sleep(1000, 2000);
        }

        return 600;
    }

    @Override
    public boolean canExecute() {
        int progress = QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT);
        return progress >= FOURTH_FLOOR_START && progress < FOURTH_FLOOR_COMPLETE;
    }
}