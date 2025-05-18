package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GetNieveAssignmentState extends AbstractState {

    public GetNieveAssignmentState(UIMQuestCape script) {
        super(script, "GetNieveAssignmentState");
    }

    @Override
    public int execute() {
        Logger.log("Getting Slayer task from Nieve - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}