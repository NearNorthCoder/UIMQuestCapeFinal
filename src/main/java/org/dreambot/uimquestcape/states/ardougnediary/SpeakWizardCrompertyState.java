package org.dreambot.uimquestcape.states.ardougnediary;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class SpeakWizardCrompertyState extends AbstractState {

    public SpeakWizardCrompertyState(UIMQuestCape script) {
        super(script, "SpeakWizardCrompertyState");
    }

    @Override
    public int execute() {
        Logger.log("Speaking to Wizard Cromperty - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}