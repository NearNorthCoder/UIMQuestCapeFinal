package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class SpeakToBanditLeaderState extends AbstractState {

    public SpeakToBanditLeaderState(UIMQuestCape script) {
        super(script, "SpeakToBanditLeaderState");
    }

    @Override
    public int execute() {
        Logger.log("Speaking to Bandit Leader");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}