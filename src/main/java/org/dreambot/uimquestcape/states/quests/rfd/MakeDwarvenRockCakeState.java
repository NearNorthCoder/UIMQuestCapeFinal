package org.dreambot.uimquestcape.states.quests.rfd;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class MakeDwarvenRockCakeState extends AbstractState {

    public MakeDwarvenRockCakeState(UIMQuestCape script) {
        super(script, "MakeDwarvenRockCakeState");
    }

    @Override
    public int execute() {
        Logger.log("Making Dwarven rock cake - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}