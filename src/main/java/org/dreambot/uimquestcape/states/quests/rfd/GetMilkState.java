package org.dreambot.uimquestcape.states.quests.rfd;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GetMilkState extends AbstractState {

    public GetMilkState(UIMQuestCape script) {
        super(script, "GetMilkState");
    }

    @Override
    public int execute() {
        Logger.log("Getting milk for RFD - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}