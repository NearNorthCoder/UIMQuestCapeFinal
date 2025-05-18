package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for returning to Gnome Stronghold during Monkey Madness quest
 */
public class ReturnToGnomeStrongholdState extends AbstractState {

    public ReturnToGnomeStrongholdState(UIMQuestCape script) {
        super(script, "ReturnToGnomeStrongholdState");
    }

    @Override
    public int execute() {
        Logger.log("Returning to Gnome Stronghold - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}