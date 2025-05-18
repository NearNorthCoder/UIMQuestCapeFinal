package org.dreambot.uimquestcape.states.wintertodt;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TrainFiremakingState extends AbstractState {

    public TrainFiremakingState(UIMQuestCape script) {
        super(script, "TrainFiremakingState");
    }

    @Override
    public int execute() {
        Logger.log("Training Firemaking to level 50 - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}