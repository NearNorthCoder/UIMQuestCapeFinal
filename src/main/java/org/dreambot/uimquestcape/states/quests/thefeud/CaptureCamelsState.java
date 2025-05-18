package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class CaptureCamelsState extends AbstractState {

    public CaptureCamelsState(UIMQuestCape script) {
        super(script, "CaptureCamelsState");
    }

    @Override
    public int execute() {
        Logger.log("Capturing camels");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}