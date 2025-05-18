package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for completing Monkey Madness quest
 */
public class MonkeyMadnessCompleteState extends AbstractState {

    public MonkeyMadnessCompleteState(UIMQuestCape script) {
        super(script, "MonkeyMadnessCompleteState");
    }

    @Override
    public int execute() {
        Logger.log("Completing Monkey Madness quest - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}