package org.dreambot.uimquestcape.states.quests.rfd;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class MineClayState extends AbstractState {

    public MineClayState(UIMQuestCape script) {
        super(script, "MineClayState");
    }

    @Override
    public int execute() {
        Logger.log("Mining clay for RFD - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}