package org.dreambot.uimquestcape.states.barbassault;

import org.dreambot.api.container.impl.Inventory;
import org.dreambot.api.container.impl.equipment.Equipment;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.NavigationHelper;

/**
 * State for purchasing the Fighter Torso reward from Barbarian Assault
 */
public class PurchaseFighterTorsoState extends AbstractState {

    private static final Area BARBARIAN_ASSAULT_AREA = new Area(2520, 3573, 2565, 3538);
    private static final Area REWARD_ROOM_AREA = new Area(2532, 3575, 2545, 3566);
    
    private final NavigationHelper navigation;
    
    public PurchaseFighterTorsoState(UIMQuestCape script) {
        super(script, "PurchaseFighterTorsoState");
        this.navigation = new NavigationHelper(script);
    }
    
    @Override
    public int execute() {
        // If we already have fighter torso, complete the state
        if (hasFighterTorso()) {
            Logger.log("Already have Fighter torso");
            complete();
            return 600;
        }
        
        // If we don't have enough honor points, can't execute
        if (!hasEnoughHonorPoints()) {
            Logger.log("Not enough honor points to purchase Fighter torso");
            return 1000;
        }
        
        // If we're not at Barbarian Assault, walk there
        if (!BARBARIAN_ASSAULT_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to Barbarian Assault");
            Walking.walk(BARBARIAN_ASSAULT_AREA.getCenter());
            return 1000;
        }
        
        // If we're not in reward room, walk there
        if (!REWARD_ROOM_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to reward room");
            Walking.walk(REWARD_ROOM_AREA.getCenter());
            return 1000;
        }
        
        // Handle dialogues if active
        if (Dialogues.inDialogue()) {
            return handleDialogue();
        }
        
        // Talk to rewards trader
        NPC rewardsTrader = NPCs.closest(npc -> 
            npc != null && 
            npc.getName() != null && 
            npc.getName().contains("Commander Connad") && 
            npc.hasAction("Trade"));
        
        if (rewardsTrader != null) {
            Logger.log("Talking to rewards trader");
            rewardsTrader.interact("Trade");
            Sleep.sleepUntil(Dialogues::inDialogue, 5000);
            return 600;
        }
        
        return 600;
    }
    
    private int handleDialogue() {
        String[] options = Dialogues.getOptions();
        
        if (options != null) {
            // Look for fighter torso option
            for (int i = 0; i < options.length; i++) {
                if (options[i].contains("Fighter torso")) {
                    Dialogues.clickOption(i + 1);
                    Sleep.sleep(600, 1200);
                    return 600;
                }
            }
            
            // If we don't see fighter torso option, look for "Torso" category
            for (int i = 0; i < options.length; i++) {
                if (options[i].contains("Torso") || options[i].contains("Armour")) {
                    Dialogues.clickOption(i + 1);
                    Sleep.sleep(600, 1200);
                    return 600;
                }
            }
            
            // If we don't see that either, click first option
            Dialogues.clickOption(1);
            return 600;
        }
        
        // Continue dialogue
        if (Dialogues.canContinue()) {
            Dialogues.clickContinue();
            
            // After continuing, check if we got the fighter torso
            if (hasFighterTorso()) {
                Logger.log("Successfully purchased Fighter torso!");
                complete();
            }
            
            return 600;
        }
        
        return 600;
    }
    
    private boolean hasFighterTorso() {
        return Equipment.contains("Fighter torso") || Inventory.contains("Fighter torso");
    }
    
    private boolean hasEnoughHonorPoints() {
        // Need 375 honor points in each role
        // This would check a Barbarian Assault points varbit or similar
        return true; // Placeholder - always return true for now
    }
    
    @Override
    public boolean canExecute() {
        return !hasFighterTorso() && hasEnoughHonorPoints();
    }
}
