package org.dreambot.uimquestcape.states.quests.rfd;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class DwarfSubquestCompletedState extends AbstractState {

    public DwarfSubquestCompletedState(UIMQuestCape script) {
        super(script, "DwarfSubquestCompletedState");
    }

    @Override
    public int execute() {
        Logger.log("Dwarf subquest completed - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}