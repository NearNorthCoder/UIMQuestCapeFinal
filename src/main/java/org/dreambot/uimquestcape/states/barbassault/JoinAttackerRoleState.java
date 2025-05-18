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
 * State for joining the Attacker role in Barbarian Assault
 */
public class JoinAttackerRoleState extends AbstractState {

    public JoinAttackerRoleState(UIMQuestCape script) {
        super(script, "JoinAttackerRoleState");
    }

    @Override
    public int execute() {
        // Check if we're already in the Attacker room
        if (BarbarianAssaultHelper.isInRoleRoom("attacker")) {
            Logger.log("Already in Attacker room");
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

        // Talk to Attacker captain
        if (BarbarianAssaultHelper.talkToRoleCaptain("Attacker")) {
            Logger.log("Talking to Attacker captain");
            return 1000;
        }

        // Enter Attacker room
        if (BarbarianAssaultHelper.enterRoleRoom("Attacker")) {
            Logger.log("Entering Attacker room");
            Sleep.sleepUntil(() -> BarbarianAssaultHelper.isInRoleRoom("attacker"), 5000);
            if (BarbarianAssaultHelper.isInRoleRoom("attacker")) {
                complete();
            }
        }

        return 1000;
    }

    @Override
    public boolean canExecute() {
        return !BarbarianAssaultHelper.isInRoleRoom("attacker");
    }
}