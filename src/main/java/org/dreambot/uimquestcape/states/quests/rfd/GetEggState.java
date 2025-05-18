package org.dreambot.uimquestcape.states.quests.rfd;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GetEggState extends AbstractState {

    public GetEggState(UIMQuestCape script) {
        super(script, "GetEggState");
    }

    @Override
    public int execute() {
        Logger.log("Getting egg for RFD - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}