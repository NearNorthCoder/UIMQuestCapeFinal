package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GatherHelmetComponentsState extends AbstractState {

    public GatherHelmetComponentsState(UIMQuestCape script) {
        super(script, "GatherHelmetComponentsState");
    }

    @Override
    public int execute() {
        Logger.log("Gathering Slayer Helmet components - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}