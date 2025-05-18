package org.dreambot.uimquestcape.states.earlyessentials;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class BuyAirRunesState extends AbstractState {

    private static final Area RUNE_SHOP_AREA = new Area(
            new Tile(3251, 3403, 0),
            new Tile(3255, 3399, 0)
    );

    private static final int AIR_RUNES_TO_BUY = 300;

    public BuyAirRunesState(UIMQuestCape script) {
        super(script, "BuyAirRunesState");
    }

    @Override
    public int execute() {
        // Check if we already have air runes
        if (Inventory.contains("Air rune")) {
            Logger.log("Already have air runes");
            complete();
            return 600;
        }

        // Check if we have enough money
        if (Inventory.count("Coins") < 300) {
            Logger.log("Not enough coins to buy air runes");
            return 600;
        }

        // Travel to rune shop if not there
        if (!RUNE_SHOP_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to rune shop");
            Walking.walk(RUNE_SHOP_AREA.getCenter());
            return 1000;
        }

        // Handle dialogues if active
        if (Dialogues.inDialogue()) {
            return handleDialogue();
        }

        // Find shop owner
        NPC shopOwner = NPCs.closest("Aubury");
        if (shopOwner != null) {
            Logger.log("Talking to rune shop owner");
            shopOwner.interact("Trade");
            Sleep.sleepUntil(Dialogues::inDialogue, 5000);
            return 600;
        }

        return 600;
    }

    private int handleDialogue() {
        // Buy air runes
        Logger.log("Buying air runes");

        // Simplified placeholder implementation - would need proper shop interface navigation
        if (Dialogues.canContinue()) {
            Dialogues.clickContinue();
        }

        // Check if we got the runes
        if (Inventory.contains("Air rune")) {
            complete();
        }

        return 600;
    }

    @Override
    public boolean canExecute() {
        return !Inventory.contains("Air rune") && Inventory.count("Coins") >= 300;
    }
}