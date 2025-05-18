package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for returning to King Narnode during Monkey Madness quest
 */
public class ReturnToNarnodeState extends AbstractState {

    public ReturnToNarnodeState(UIMQuestCape script) {
        super(script, "ReturnToNarnodeState");
    }

    @Override
    public int execute() {
        Logger.log("Returning to King Narnode - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}