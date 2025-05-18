package org.dreambot.uimquestcape.states.earlyessentials;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToBarbarianVillageState extends AbstractState {

    private static final Area BARBARIAN_VILLAGE_AREA = new Area(
            new Tile(3072, 3433, 0),
            new Tile(3089, 3415, 0)
    );

    public TravelToBarbarianVillageState(UIMQuestCape script) {
        super(script, "TravelToBarbarianVillageState");
    }

    @Override
    public int execute() {
        // Check if we're already at Barbarian Village
        if (BARBARIAN_VILLAGE_AREA.contains(Players.getLocal())) {
            Logger.log("Reached Barbarian Village");
            complete();
            return 600;
        }

        // Walk to Barbarian Village
        Logger.log("Walking to Barbarian Village");
        Walking.walkExact(BARBARIAN_VILLAGE_AREA.getCenter());

        // Wait until arrived or stopped moving
        Sleep.sleepUntil(() ->
                        BARBARIAN_VILLAGE_AREA.contains(Players.getLocal()) ||
                                !Players.getLocal().isMoving(),
                10000
        );

        return 1000;
    }

    @Override
    public boolean canExecute() {
        return !BARBARIAN_VILLAGE_AREA.contains(Players.getLocal());
    }
}