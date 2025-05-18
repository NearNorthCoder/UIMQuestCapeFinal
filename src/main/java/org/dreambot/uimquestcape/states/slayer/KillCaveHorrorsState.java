package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class KillCaveHorrorsState extends AbstractState {

    public KillCaveHorrorsState(UIMQuestCape script) {
        super(script, "KillCaveHorrorsState");
    }

    @Override
    public int execute() {
        Logger.log("Killing Cave Horrors - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}