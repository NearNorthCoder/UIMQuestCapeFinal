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

public class CompleteFirstFloorState extends AbstractState {

    private static final int STRONGHOLD_VARBIT = 853;
    private static final int FIRST_FLOOR_START = 1;
    private static final int FIRST_FLOOR_COMPLETE = 2;

    private static final Area FIRST_FLOOR_AREA = new Area(1856, 5248, 1920, 5184, 1);

    public CompleteFirstFloorState(UIMQuestCape script) {
        super(script, "CompleteFirstFloorState");
    }

    @Override
    public int execute() {
        // Check if first floor is already completed
        if (QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) >= FIRST_FLOOR_COMPLETE) {
            Logger.log("First floor already completed");
            complete();
            return 600;
        }

        // Make sure we're on the first floor
        if (!FIRST_FLOOR_AREA.contains(Players.getLocal())) {
            Logger.log("Not on first floor, need to enter the stronghold first");
            return 1000;
        }

        // Navigate to the reward chest
        GameObject rewardChest = GameObjects.closest("Reward chest");
        if (rewardChest != null) {
            Logger.log("Opening reward chest");
            rewardChest.interact("Open");
            Sleep.sleepUntil(() -> QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) >= FIRST_FLOOR_COMPLETE, 5000);

            if (QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) >= FIRST_FLOOR_COMPLETE) {
                complete();
            }

            return 600;
        }

        // Navigate through corridors (simplified)
        Logger.log("Navigating through first floor");

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
        return progress >= FIRST_FLOOR_START && progress < FIRST_FLOOR_COMPLETE;
    }
}