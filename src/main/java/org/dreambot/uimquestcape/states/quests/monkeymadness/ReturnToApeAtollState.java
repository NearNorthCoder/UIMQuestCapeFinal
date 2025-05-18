package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for returning to Ape Atoll during Monkey Madness quest
 */
public class ReturnToApeAtollState extends AbstractState {

    public ReturnToApeAtollState(UIMQuestCape script) {
        super(script, "ReturnToApeAtollState");
    }

    @Override
    public int execute() {
        Logger.log("Returning to Ape Atoll - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}