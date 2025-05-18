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
 * State for joining the Defender role in Barbarian Assault
 */
public class JoinDefenderRoleState extends AbstractState {

    public JoinDefenderRoleState(UIMQuestCape script) {
        super(script, "JoinDefenderRoleState");
    }

    @Override
    public int execute() {
        // Check if we're already in the Defender room
        if (BarbarianAssaultHelper.isInRoleRoom("defender")) {
            Logger.log("Already in Defender room");
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

        // Talk to Defender captain
        if (BarbarianAssaultHelper.talkToRoleCaptain("Defender")) {
            Logger.log("Talking to Defender captain");
            return 1000;
        }

        // Enter Defender room
        if (BarbarianAssaultHelper.enterRoleRoom("Defender")) {
            Logger.log("Entering Defender room");
            Sleep.sleepUntil(() -> BarbarianAssaultHelper.isInRoleRoom("defender"), 5000);
            if (BarbarianAssaultHelper.isInRoleRoom("defender")) {
                complete();
            }
        }

        return 1000;
    }

    @Override
    public boolean canExecute() {
        return !BarbarianAssaultHelper.isInRoleRoom("defender");
    }
}