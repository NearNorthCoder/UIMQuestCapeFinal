package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TheFeutdStartState extends AbstractState {

    public TheFeutdStartState(UIMQuestCape script) {
        super(script, "TheFeutdStartState");
    }

    @Override
    public int execute() {
        Logger.log("Starting The Feud quest");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}