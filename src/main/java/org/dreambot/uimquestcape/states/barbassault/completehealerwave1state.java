package org.dreambot.uimquestcape.states.barbassault;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for completing Wave 1 as a Healer in Barbarian Assault
 */
public class CompleteHealerWave1State extends AbstractState {
    
    private boolean waveStarted = false;
    private boolean waveCompleted = false;
    
    public CompleteHealerWave1State(UIMQuestCape script) {
        super(script, "CompleteHealerWave1State");
    }
    
    @Override
    public int execute() {
        // Check if we're in the correct role room
        if (!BarbarianAssaultHelper.isInRoleRoom("healer")) {
            Logger.log("Not in Healer room, need to join first");
            return 1000;
        }
        
        // Handle game over or wave complete
        if (handleEndOfWaveInterfaces()) {
            return 600;
        }
        
        // Check if wave is completed
        if (waveCompleted) {
            Logger.log("Wave 1 completed as Healer");
            complete();
            return 600;
        }
        
        // Handle dialogues if active
        if (Dialogues.inDialogue()) {
            Dialogues.clickContinue();
            return 600;
        }
        
        // If wave hasn't started yet, wait for team formation
        if (!waveStarted) {
            return handleTeamFormation();
        }
        
        // Perform Healer role duties
        return performHealerDuties();
    }
    
    private int handleTeamFormation() {
        // Check if enough teammates have joined
        if (BarbarianAssaultHelper.hasTeamFormedForWave()) {
            Logger.log("Team has formed, starting wave");
            waveStarted = true;
            return 600;
        }
        
        Logger.log("Waiting for team to form");
        return 1000;
    }
    
    private int performHealerDuties() {
        // Get the call (what poison food to use)
        String call = BarbarianAssaultHelper.getCallForRole("healer");
        
        // Make poison food and heal teammates based on call
        if (call.isEmpty()) {
            Logger.log("No call visible yet");
            return 600;
        }
        
        Logger.log("Healer call: " + call);
        
        // Heal teammates when needed
        if (shouldHealTeammates()) {
            healTeammates();
            return 600;
        }
        
        // Make appropriate poison food
        if (!hasPoisonFood(call)) {
            makePoisonFood(call);
            return 600;
        }
        
        // Poison healers
        NPC penanceHealer = NPCs.closest(npc -> 
            npc != null && 
            npc.getName() != null && 
            npc.getName().contains("Penance Healer") && 
            npc.hasAction("Use"));
        
        if (penanceHealer != null) {
            poisonHealer(call, penanceHealer);
            return 600;
        }
        
        return 600;
    }
    
    private boolean shouldHealTeammates() {
        // Check if teammates need healing
        return false; // Placeholder - would check teammates' health
    }
    
    private void healTeammates() {
        Logger.log("Healing teammates");
        // Find teammates to heal
    }
    
    private boolean hasPoisonFood(String foodType) {
        return Inventory.contains(foodType + " poison");
    }
    
    private void makePoisonFood(String foodType) {
        Logger.log("Making " + foodType + " poison");
        // Logic to make poison food
    }
    
    private void poisonHealer(String foodType, NPC healer) {
        Logger.log("Poisoning healer with " + foodType);
        healer.interact("Use");
        Sleep.sleep(600, 1200);
    }
    
    private boolean handleEndOfWaveInterfaces() {
        // Check for game over interface
        if (BarbarianAssaultHelper.isGameOver()) {
            Logger.log("Game over detected");
            BarbarianAssaultHelper.closeGameOverInterface();
            return true;
        }
        
        // Check for wave completion interface
        if (BarbarianAssaultHelper.isWaveCompleted()) {
            Logger.log("Wave completed");
            waveCompleted = true;
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean canExecute() {
        return BarbarianAssaultHelper.isInRoleRoom("healer") && !waveCompleted;
    }
}
