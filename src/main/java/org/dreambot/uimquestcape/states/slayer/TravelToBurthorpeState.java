package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToBurthorpeState extends AbstractState {

    public TravelToBurthorpeState(UIMQuestCape script) {
        super(script, "TravelToBurthorpeState");
    }

    @Override
    public int execute() {
        Logger.log("Traveling to Burthorpe - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}