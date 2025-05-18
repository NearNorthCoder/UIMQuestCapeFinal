package org.dreambot.uimquestcape.states.barbassault;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for joining the Healer role in Barbarian Assault
 */
public class JoinHealerRoleState extends AbstractState {

    public JoinHealerRoleState(UIMQuestCape script) {
        super(script, "JoinHealerRoleState");
    }

    @Override
    public int execute() {
        // Check if we're already in the Healer room
        if (BarbarianAssaultHelper.isInRoleRoom("healer")) {
            Logger.log("Already in Healer room");
            complete();
            return 600;
        }

        // Check if we're in the lobby
        if (!BarbarianAssaultHelper.isInLobby()) {
            Logger.log("Not in Barbarian Assault lobby, walking there");
            // Walk to lobby implementation
            return 1000;
        }

        // Handle dialogues if active
        if (Dialogues.inDialogue()) {
            BarbarianAssaultHelper.handleRoleJoinDialogue();
            return 600;
        }

        // Talk to Healer captain
        if (BarbarianAssaultHelper.talkToRoleCaptain("Healer")) {
            Logger.log("Talking to Healer captain");
            return 1000;
        }

        // Enter Healer room
        if (BarbarianAssaultHelper.enterRoleRoom("Healer")) {
            Logger.log("Entering Healer room");
            Sleep.sleepUntil(() -> BarbarianAssaultHelper.isInRoleRoom("healer"), 5000);
            if (BarbarianAssaultHelper.isInRoleRoom("healer")) {
                complete();
            }
        }

        return 1000;
    }

    @Override
    public boolean canExecute() {
        return !BarbarianAssaultHelper.isInRoleRoom("healer");
    }
}