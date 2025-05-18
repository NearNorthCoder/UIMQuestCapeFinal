package org.dreambot.uimquestcape.states.earlyessentials;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToGrandExchangeState extends AbstractState {

    private static final Area GRAND_EXCHANGE_AREA = new Area(
            new Tile(3158, 3495, 0),
            new Tile(3173, 3483, 0)
    );

    public TravelToGrandExchangeState(UIMQuestCape script) {
        super(script, "TravelToGrandExchangeState");
    }

    @Override
    public int execute() {
        // Check if we're already at the Grand Exchange
        if (GRAND_EXCHANGE_AREA.contains(Players.getLocal())) {
            Logger.log("Reached Grand Exchange");
            complete();
            return 600;
        }

        // Walk to Grand Exchange
        Logger.log("Walking to Grand Exchange");
        Walking.walkExact(GRAND_EXCHANGE_AREA.getCenter());

        // Wait until arrived or stopped moving
        Sleep.sleepUntil(() ->
                        GRAND_EXCHANGE_AREA.contains(Players.getLocal()) ||
                                !Players.getLocal().isMoving(),
                10000
        );

        return 1000;
    }

    @Override
    public boolean canExecute() {
        return !GRAND_EXCHANGE_AREA.contains(Players.getLocal());
    }
}