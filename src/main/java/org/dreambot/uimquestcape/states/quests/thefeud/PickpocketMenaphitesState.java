package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class PickpocketMenaphitesState extends AbstractState {

    public PickpocketMenaphitesState(UIMQuestCape script) {
        super(script, "PickpocketMenaphitesState");
    }

    @Override
    public int execute() {
        Logger.log("Pickpocketing Menaphites");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}