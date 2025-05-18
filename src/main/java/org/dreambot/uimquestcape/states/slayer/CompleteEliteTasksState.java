package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class CompleteEliteTasksState extends AbstractState {

    public CompleteEliteTasksState(UIMQuestCape script) {
        super(script, "CompleteEliteTasksState");
    }

    @Override
    public int execute() {
        Logger.log("Completing elite slayer tasks - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}