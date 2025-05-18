package org.dreambot.uimquestcape.states.ardougnediary;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class FillBucketState extends AbstractState {

    public FillBucketState(UIMQuestCape script) {
        super(script, "FillBucketState");
    }

    @Override
    public int execute() {
        Logger.log("Filling bucket - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}