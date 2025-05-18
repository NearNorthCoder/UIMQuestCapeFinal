package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GetVannakaAssignmentState extends AbstractState {

    public GetVannakaAssignmentState(UIMQuestCape script) {
        super(script, "GetVannakaAssignmentState");
    }

    @Override
    public int execute() {
        Logger.log("Getting Slayer task from Vannaka - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}