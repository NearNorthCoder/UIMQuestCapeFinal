package org.dreambot.uimquestcape.states.lumbridge;

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
import org.dreambot.uimquestcape.util.NavigationHelper;

/**
 * Buys a hammer from Lumbridge General Store
 */
public class BuyHammerState extends AbstractState {

    private static final Area GENERAL_STORE_AREA = new Area(
            new Tile(3206, 3252, 0),
            new Tile(3216, 3244, 0)
    );

    private final NavigationHelper navigation;

    public BuyHammerState(UIMQuestCape script) {
        super(script, "BuyHammerState");
        this.navigation = new NavigationHelper(script);
    }

    @Override
    public int execute() {
        // If we already have a hammer, complete the state
        if (Inventory.contains("Hammer")) {
            Logger.log("Already have a hammer");
            complete();
            return 600;
        }

        // If we're not at the general store, walk there
        if (!GENERAL_STORE_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to Lumbridge general store");
            Walking.walk(GENERAL_STORE_AREA.getCenter());
            return 1000;
        }

        // Handle dialogues if they're open
        if (Dialogues.inDialogue()) {
            return handleDialogue();
        }

        // Check if we have enough coins
        if (!hasEnoughCoins()) {
            Logger.log("Not enough coins to buy a hammer, need to get more money");
            return 600;
        }

        // Talk to shop keeper
        NPC shopkeeper = NPCs.closest("Shop keeper");
        if (shopkeeper != null) {
            Logger.log("Talking to shop keeper");
            shopkeeper.interact("Trade");
            Sleep.sleepUntil(Dialogues::inDialogue, 5000);
            return 600;
        }

        return 600;
    }

    private int handleDialogue() {
        // Check if shop interface is open
        if (isShopOpen()) {
            // Buy hammer
            if (buyHammer()) {
                Logger.log("Bought hammer");
                Sleep.sleepUntil(() -> Inventory.contains("Hammer"), 3000);
                if (Inventory.contains("Hammer")) {
                    complete();
                }
            }
            return 600;
        }

        // Handle standard dialogue
        if (Dialogues.canContinue()) {
            Dialogues.clickContinue();
            return 600;
        }

        return 600;
    }

    private boolean isShopOpen() {
        // Check if shop interface is open
        return false; // Placeholder - needs implementation with proper widget ID
    }

    private boolean buyHammer() {
        // Buy hammer from shop
        return false; // Placeholder - needs implementation
    }

    private boolean hasEnoughCoins() {
        // Check if we have at least 5 gp to buy hammer
        return Inventory.count("Coins") >= 5;
    }

    @Override
    public boolean canExecute() {
        return !Inventory.contains("Hammer");
    }
}