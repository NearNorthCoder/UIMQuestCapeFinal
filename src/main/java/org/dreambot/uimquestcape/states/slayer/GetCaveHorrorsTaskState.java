package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GetCaveHorrorsTaskState extends AbstractState {

    public GetCaveHorrorsTaskState(UIMQuestCape script) {
        super(script, "GetCaveHorrorsTaskState");
    }

    @Override
    public int execute() {
        Logger.log("Getting Cave Horrors slayer task - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}