package org.dreambot.uimquestcape.states.ardougnediary;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class PickpocketStallState extends AbstractState {

    public PickpocketStallState(UIMQuestCape script) {
        super(script, "PickpocketStallState");
    }

    @Override
    public int execute() {
        Logger.log("Pickpocketing stall - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}