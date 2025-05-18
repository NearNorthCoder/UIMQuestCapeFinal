package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class BlackjackBanditsState extends AbstractState {

    public BlackjackBanditsState(UIMQuestCape script) {
        super(script, "BlackjackBanditsState");
    }

    @Override
    public int execute() {
        Logger.log("Blackjacking bandits");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}