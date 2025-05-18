package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GetDuradelAssignmentState extends AbstractState {

    public GetDuradelAssignmentState(UIMQuestCape script) {
        super(script, "GetDuradelAssignmentState");
    }

    @Override
    public int execute() {
        Logger.log("Getting Slayer task from Duradel - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}