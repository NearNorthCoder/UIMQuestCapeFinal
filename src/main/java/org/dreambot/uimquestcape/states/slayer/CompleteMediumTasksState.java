package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class CompleteMediumTasksState extends AbstractState {

    public CompleteMediumTasksState(UIMQuestCape script) {
        super(script, "CompleteMediumTasksState");
    }

    @Override
    public int execute() {
        Logger.log("Completing medium slayer tasks - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}