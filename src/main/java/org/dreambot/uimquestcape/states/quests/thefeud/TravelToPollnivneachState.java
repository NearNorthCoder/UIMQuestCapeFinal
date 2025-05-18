package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToPollnivneachState extends AbstractState {

    public TravelToPollnivneachState(UIMQuestCape script) {
        super(script, "TravelToPollnivneachState");
    }

    @Override
    public int execute() {
        Logger.log("Traveling to Pollnivneach for The Feud quest");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}