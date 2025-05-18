package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class SpeakToDrunkenAliState extends AbstractState {

    public SpeakToDrunkenAliState(UIMQuestCape script) {
        super(script, "SpeakToDrunkenAliState");
    }

    @Override
    public int execute() {
        Logger.log("Speaking to Drunken Ali");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}