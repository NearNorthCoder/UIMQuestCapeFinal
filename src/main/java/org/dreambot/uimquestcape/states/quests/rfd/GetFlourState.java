package org.dreambot.uimquestcape.states.quests.rfd;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GetFlourState extends AbstractState {

    public GetFlourState(UIMQuestCape script) {
        super(script, "GetFlourState");
    }

    @Override
    public int execute() {
        Logger.log("Getting flour for RFD - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}