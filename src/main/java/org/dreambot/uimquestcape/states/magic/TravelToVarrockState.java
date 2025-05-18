package org.dreambot.uimquestcape.states.magic;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToVarrockState extends AbstractState {

    public TravelToVarrockState(UIMQuestCape script) {
        super(script, "TravelToVarrockState");
    }

    @Override
    public int execute() {
        Logger.log("Traveling to Varrock - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}