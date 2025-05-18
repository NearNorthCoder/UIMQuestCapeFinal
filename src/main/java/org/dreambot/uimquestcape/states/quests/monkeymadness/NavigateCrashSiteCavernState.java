package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for navigating through crash site cavern during Monkey Madness quest
 */
public class NavigateCrashSiteCavernState extends AbstractState {

    public NavigateCrashSiteCavernState(UIMQuestCape script) {
        super(script, "NavigateCrashSiteCavernState");
    }

    @Override
    public int execute() {
        Logger.log("Navigating through crash site cavern - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}