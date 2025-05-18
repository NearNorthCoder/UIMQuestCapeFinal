package org.dreambot.uimquestcape.states.ardougnediary;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToArdougneState extends AbstractState {

    public TravelToArdougneState(UIMQuestCape script) {
        super(script, "TravelToArdougneState");
    }

    @Override
    public int execute() {
        Logger.log("Traveling to Ardougne - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}