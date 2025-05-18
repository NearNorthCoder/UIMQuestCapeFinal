package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for completing the platform puzzle during Monkey Madness quest
 */
public class CompletePlatformPuzzleState extends AbstractState {

    public CompletePlatformPuzzleState(UIMQuestCape script) {
        super(script, "CompletePlatformPuzzleState");
    }

    @Override
    public int execute() {
        Logger.log("Completing platform puzzle - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}