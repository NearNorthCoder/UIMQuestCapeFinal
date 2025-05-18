package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GetChaeldarAssignmentState extends AbstractState {

    public GetChaeldarAssignmentState(UIMQuestCape script) {
        super(script, "GetChaeldarAssignmentState");
    }

    @Override
    public int execute() {
        Logger.log("Getting Slayer task from Chaeldar - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}