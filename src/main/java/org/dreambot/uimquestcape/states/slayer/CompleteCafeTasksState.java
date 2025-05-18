package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class CompleteCafeTasksState extends AbstractState {

    public CompleteCafeTasksState(UIMQuestCape script) {
        super(script, "CompleteCafeTasksState");
    }

    @Override
    public int execute() {
        Logger.log("Completing cafe slayer tasks - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}