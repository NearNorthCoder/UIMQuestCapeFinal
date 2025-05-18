package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToShiloVillageState extends AbstractState {

    public TravelToShiloVillageState(UIMQuestCape script) {
        super(script, "TravelToShiloVillageState");
    }

    @Override
    public int execute() {
        Logger.log("Traveling to Shilo Village - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}