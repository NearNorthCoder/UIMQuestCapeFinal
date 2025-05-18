package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class CompleteEasyTasksState extends AbstractState {

    public CompleteEasyTasksState(UIMQuestCape script) {
        super(script, "CompleteEasyTasksState");
    }

    @Override
    public int execute() {
        Logger.log("Completing easy slayer tasks - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}