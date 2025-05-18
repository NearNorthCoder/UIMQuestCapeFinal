package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class DefeatBanditChampionState extends AbstractState {

    public DefeatBanditChampionState(UIMQuestCape script) {
        super(script, "DefeatBanditChampionState");
    }

    @Override
    public int execute() {
        Logger.log("Defeating Bandit Champion");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}