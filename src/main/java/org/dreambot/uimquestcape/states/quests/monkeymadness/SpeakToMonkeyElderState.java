package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for speaking to the monkey elder during Monkey Madness quest
 */
public class SpeakToMonkeyElderState extends AbstractState {

    public SpeakToMonkeyElderState(UIMQuestCape script) {
        super(script, "SpeakToMonkeyElderState");
    }

    @Override
    public int execute() {
        Logger.log("Speaking to monkey elder - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}