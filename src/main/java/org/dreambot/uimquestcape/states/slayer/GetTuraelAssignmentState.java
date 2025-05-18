package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GetTuraelAssignmentState extends AbstractState {

    public GetTuraelAssignmentState(UIMQuestCape script) {
        super(script, "GetTuraelAssignmentState");
    }

    @Override
    public int execute() {
        Logger.log("Getting Slayer task from Turael - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}