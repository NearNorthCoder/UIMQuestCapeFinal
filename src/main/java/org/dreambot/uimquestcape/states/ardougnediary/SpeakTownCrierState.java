package org.dreambot.uimquestcape.states.ardougnediary;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class SpeakTownCrierState extends AbstractState {

    public SpeakTownCrierState(UIMQuestCape script) {
        super(script, "SpeakTownCrierState");
    }

    @Override
    public int execute() {
        Logger.log("Speaking to Town Crier - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}