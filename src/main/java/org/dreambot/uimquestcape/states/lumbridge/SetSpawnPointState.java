package org.dreambot.uimquestcape.states.lumbridge;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.QuestVarbitManager;

public class SetSpawnPointState extends AbstractState {

    private static final Area LUMBRIDGE_GUIDE_AREA = new Area(
            new Tile(3230, 3235, 0),
            new Tile(3239, 3223, 0)
    );

    private static final int SPAWN_POINT_CONFIG = 1131;
    private static final int LUMBRIDGE_SPAWN = 0;

    private boolean talkedToGuide = false;
    private boolean spawnPointSet = false;

    public SetSpawnPointState(UIMQuestCape script) {
        super(script, "SetSpawnPointState");
    }

    @Override
    public int execute() {
        // Check if spawn is already set to Lumbridge
        if (PlayerSettings.getConfig(SPAWN_POINT_CONFIG) == LUMBRIDGE_SPAWN) {
            Logger.log("Spawn point already set to Lumbridge");
            spawnPointSet = true;
            complete();
            return 600;
        }

        // Handle dialogues if active
        if (Dialogues.inDialogue()) {
            return handleDialogue();
        }

        // If we're not at Lumbridge Guide area, walk there
        if (!LUMBRIDGE_GUIDE_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to Lumbridge Guide");
            Walking.walk(LUMBRIDGE_GUIDE_AREA.getRandomTile());
            return 1000;
        }

        // Talk to Lumbridge Guide
        NPC lumbridgeGuide = NPCs.closest("Lumbridge Guide");
        if (lumbridgeGuide != null) {
            Logger.log("Talking to Lumbridge Guide");
            lumbridgeGuide.interact("Talk-to");
            Sleep.sleepUntil(Dialogues::inDialogue, 5000);
            return 600;
        }

        return 600;
    }

    private int handleDialogue() {
        String npcName = Dialogues.getNPCName();

        // Mark that we've talked to the Lumbridge Guide
        if ("Lumbridge Guide".equals(npcName)) {
            talkedToGuide = true;

            // Check dialogue options
            String[] options = Dialogues.getOptions();
            if (options != null) {
                // Look for option related to spawn point
                for (int i = 0; i < options.length; i++) {
                    if (options[i].contains("spawn") || options[i].contains("respawn")) {
                        Dialogues.clickOption(i + 1);
                        Sleep.sleep(600, 1000);
                        return 600;
                    }
                }

                // If no specific option found, just choose the first one
                Dialogues.clickOption(1);
                return 600;
            }
        }

        // Continue dialogue
        if (Dialogues.canContinue()) {
            Dialogues.clickContinue();
            Sleep.sleep(300, 600);

            // Check after continuing if spawn has been set
            if (PlayerSettings.getConfig(SPAWN_POINT_CONFIG) == LUMBRIDGE_SPAWN) {
                Logger.log("Spawn point set to Lumbridge");
                spawnPointSet = true;
                complete();
            }

            return 600;
        }

        return 600;
    }

    @Override
    public boolean canExecute() {
        // Can execute if we haven't set spawn point yet
        return !spawnPointSet && PlayerSettings.getConfig(SPAWN_POINT_CONFIG) != LUMBRIDGE_SPAWN;
    }
}