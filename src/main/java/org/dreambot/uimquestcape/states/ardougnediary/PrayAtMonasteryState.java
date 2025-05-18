package org.dreambot.uimquestcape.states.ardougnediary;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class PrayAtMonasteryState extends AbstractState {

    public PrayAtMonasteryState(UIMQuestCape script) {
        super(script, "PrayAtMonasteryState");
    }

    @Override
    public int execute() {
        Logger.log("Praying at Monastery - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}