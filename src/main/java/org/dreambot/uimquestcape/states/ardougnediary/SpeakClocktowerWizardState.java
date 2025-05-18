package org.dreambot.uimquestcape.states.ardougnediary;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class SpeakClocktowerWizardState extends AbstractState {

    public SpeakClocktowerWizardState(UIMQuestCape script) {
        super(script, "SpeakClocktowerWizardState");
    }

    @Override
    public int execute() {
        Logger.log("Speaking to Clocktower Wizard - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}