package org.dreambot.uimquestcape.states.wintertodt;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToZeahState extends AbstractState {

    public TravelToZeahState(UIMQuestCape script) {
        super(script, "TravelToZeahState");
    }

    @Override
    public int execute() {
        Logger.log("Traveling to Zeah - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}