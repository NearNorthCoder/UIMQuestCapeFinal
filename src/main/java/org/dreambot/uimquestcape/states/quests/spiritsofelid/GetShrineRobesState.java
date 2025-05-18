package org.dreambot.uimquestcape.states.quests.spiritsofelid;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GetShrineRobesState extends AbstractState {
    
    public GetShrineRobesState(UIMQuestCape script) {
        super(script, "GetShrineRobesState");
    }
    
    @Override
    public int execute() {
        Logger.log("Getting shrine robes");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}