package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for creating the monkey talisman during Monkey Madness quest
 */
public class CreateMonkeyTalismanState extends AbstractState {

    public CreateMonkeyTalismanState(UIMQuestCape script) {
        super(script, "CreateMonkeyTalismanState");
    }

    @Override
    public int execute() {
        Logger.log("Creating monkey talisman - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}