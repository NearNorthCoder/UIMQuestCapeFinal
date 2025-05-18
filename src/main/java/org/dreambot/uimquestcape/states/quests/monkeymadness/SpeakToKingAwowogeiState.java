package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for speaking to King Awowogei during Monkey Madness quest
 */
public class SpeakToKingAwowogeiState extends AbstractState {

    public SpeakToKingAwowogeiState(UIMQuestCape script) {
        super(script, "SpeakToKingAwowogeiState");
    }

    @Override
    public int execute() {
        Logger.log("Speaking to King Awowogei - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}