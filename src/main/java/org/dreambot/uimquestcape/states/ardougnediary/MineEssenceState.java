package org.dreambot.uimquestcape.states.ardougnediary;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class MineEssenceState extends AbstractState {

    public MineEssenceState(UIMQuestCape script) {
        super(script, "MineEssenceState");
    }

    @Override
    public int execute() {
        Logger.log("Mining Essence - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}