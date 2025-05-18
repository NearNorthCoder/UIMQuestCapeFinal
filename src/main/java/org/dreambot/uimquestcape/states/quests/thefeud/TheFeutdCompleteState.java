package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TheFeutdCompleteState extends AbstractState {

    public TheFeutdCompleteState(UIMQuestCape script) {
        super(script, "TheFeutdCompleteState");
    }

    @Override
    public int execute() {
        Logger.log("Completing The Feud quest");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}