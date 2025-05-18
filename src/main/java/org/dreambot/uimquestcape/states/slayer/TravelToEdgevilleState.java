package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToEdgevilleState extends AbstractState {

    public TravelToEdgevilleState(UIMQuestCape script) {
        super(script, "TravelToEdgevilleState");
    }

    @Override
    public int execute() {
        Logger.log("Traveling to Edgeville - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}