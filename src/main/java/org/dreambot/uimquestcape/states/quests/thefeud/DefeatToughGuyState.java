package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class DefeatToughGuyState extends AbstractState {

    public DefeatToughGuyState(UIMQuestCape script) {
        super(script, "DefeatToughGuyState");
    }

    @Override
    public int execute() {
        Logger.log("Defeating Tough Guy");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}