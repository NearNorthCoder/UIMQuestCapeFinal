package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToMosLeHarmlessState extends AbstractState {

    public TravelToMosLeHarmlessState(UIMQuestCape script) {
        super(script, "TravelToMosLeHarmlessState");
    }

    @Override
    public int execute() {
        Logger.log("Traveling to Mos Le'Harmless - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}