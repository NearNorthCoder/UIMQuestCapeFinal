package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToGnomeStrongholdState extends AbstractState {

    public TravelToGnomeStrongholdState(UIMQuestCape script) {
        super(script, "TravelToGnomeStrongholdState");
    }

    @Override
    public int execute() {
        Logger.log("Traveling to Gnome Stronghold - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}