package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class SpeakToMenaphiteLeaderState extends AbstractState {

    public SpeakToMenaphiteLeaderState(UIMQuestCape script) {
        super(script, "SpeakToMenaphiteLeaderState");
    }

    @Override
    public int execute() {
        Logger.log("Speaking to Menaphite Leader");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}