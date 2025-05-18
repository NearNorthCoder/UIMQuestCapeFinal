package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToZanarisState extends AbstractState {

    public TravelToZanarisState(UIMQuestCape script) {
        super(script, "TravelToZanarisState");
    }

    @Override
    public int execute() {
        Logger.log("Traveling to Zanaris - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}