package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for entering the temple during Monkey Madness quest
 */
public class EnterTempleState extends AbstractState {

    public EnterTempleState(UIMQuestCape script) {
        super(script, "EnterTempleState");
    }

    @Override
    public int execute() {
        Logger.log("Entering temple - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}