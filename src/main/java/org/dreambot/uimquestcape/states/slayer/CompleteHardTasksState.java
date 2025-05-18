package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class CompleteHardTasksState extends AbstractState {

    public CompleteHardTasksState(UIMQuestCape script) {
        super(script, "CompleteHardTasksState");
    }

    @Override
    public int execute() {
        Logger.log("Completing hard slayer tasks - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}