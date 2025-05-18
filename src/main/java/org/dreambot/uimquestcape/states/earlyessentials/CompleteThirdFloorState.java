package org.dreambot.uimquestcape.states.earlyessentials;

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

public class CompleteThirdFloorState extends AbstractState {

    private static final int STRONGHOLD_VARBIT = 853;
    private static final int THIRD_FLOOR_START = 3;
    private static final int THIRD_FLOOR_COMPLETE = 5;

    private static final Area THIRD_FLOOR_AREA = new Area(1856, 5248, 1920, 5184, 2);

    public CompleteThirdFloorState(UIMQuestCape script) {
        super(script, "CompleteThirdFloorState");
    }

    @Override
    public int execute() {
        // Check if third floor is already completed
        if (QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) >= THIRD_FLOOR_COMPLETE) {
            Logger.log("Third floor already completed");
            complete();
            return 600;
        }

        // Make sure we're on the third floor
        if (!THIRD_FLOOR_AREA.contains(Players.getLocal())) {
            // Find and use stairs from second floor
            GameObject stairs = GameObjects.closest(obj ->
                    obj != null &&
                            obj.getName() != null &&
                            obj.getName().contains("Ladder") &&
                            obj.hasAction("Climb-down")
            );

            if (stairs != null) {
                Logger.log("Using stairs to reach third floor");
                stairs.interact("Climb-down");
                Sleep.sleepUntil(() -> THIRD_FLOOR_AREA.contains(Players.getLocal()), 5000);
            }

            return 1000;
        }

        // Navigate to the reward chest
        GameObject rewardChest = GameObjects.closest("Reward chest");
        if (rewardChest != null) {
            Logger.log("Opening reward chest");
            rewardChest.interact("Open");
            Sleep.sleepUntil(() -> QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) >= THIRD_FLOOR_COMPLETE, 5000);

            if (QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) >= THIRD_FLOOR_COMPLETE) {
                complete();
            }

            return 600;
        }

        // Navigate through corridors (simplified)
        Logger.log("Navigating through third floor");

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
        return progress >= THIRD_FLOOR_START && progress < THIRD_FLOOR_COMPLETE;
    }
}