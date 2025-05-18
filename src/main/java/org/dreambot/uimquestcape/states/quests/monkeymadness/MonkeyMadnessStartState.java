package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for starting Monkey Madness quest
 */
public class MonkeyMadnessStartState extends AbstractState {

    public MonkeyMadnessStartState(UIMQuestCape script) {
        super(script, "MonkeyMadnessStartState");
    }

    @Override
    public int execute() {
        Logger.log("Starting Monkey Madness quest - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}