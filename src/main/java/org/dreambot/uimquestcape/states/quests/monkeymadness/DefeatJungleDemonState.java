package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for defeating the jungle demon during Monkey Madness quest
 */
public class DefeatJungleDemonState extends AbstractState {

    public DefeatJungleDemonState(UIMQuestCape script) {
        super(script, "DefeatJungleDemonState");
    }

    @Override
    public int execute() {
        Logger.log("Defeating jungle demon - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}