package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class AcquireBlackMaskState extends AbstractState {

    public AcquireBlackMaskState(UIMQuestCape script) {
        super(script, "AcquireBlackMaskState");
    }

    @Override
    public int execute() {
        Logger.log("Acquiring Black Mask - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}