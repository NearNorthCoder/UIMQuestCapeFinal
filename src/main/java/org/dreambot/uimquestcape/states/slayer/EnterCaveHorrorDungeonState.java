package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class EnterCaveHorrorDungeonState extends AbstractState {

    public EnterCaveHorrorDungeonState(UIMQuestCape script) {
        super(script, "EnterCaveHorrorDungeonState");
    }

    @Override
    public int execute() {
        Logger.log("Entering Cave Horror dungeon - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}