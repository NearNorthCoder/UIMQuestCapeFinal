package org.dreambot.uimquestcape.states.quests.rfd;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class CooksAssistantCompletedState extends AbstractState {

    public CooksAssistantCompletedState(UIMQuestCape script) {
        super(script, "CooksAssistantCompletedState");
    }

    @Override
    public int execute() {
        Logger.log("Cook's Assistant subquest completed - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}