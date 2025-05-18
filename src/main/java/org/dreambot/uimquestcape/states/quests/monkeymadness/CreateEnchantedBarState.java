package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for creating the enchanted bar during Monkey Madness quest
 */
public class CreateEnchantedBarState extends AbstractState {

    public CreateEnchantedBarState(UIMQuestCape script) {
        super(script, "CreateEnchantedBarState");
    }

    @Override
    public int execute() {
        Logger.log("Creating enchanted bar - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}