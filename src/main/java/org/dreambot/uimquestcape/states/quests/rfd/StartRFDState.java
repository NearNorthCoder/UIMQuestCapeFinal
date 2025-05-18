package org.dreambot.uimquestcape.states.quests.rfd;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class StartRFDState extends AbstractState {

    public StartRFDState(UIMQuestCape script) {
        super(script, "StartRFDState");
    }

    @Override
    public int execute() {
        Logger.log("Starting Recipe for Disaster - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}