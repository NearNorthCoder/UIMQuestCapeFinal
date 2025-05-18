package org.dreambot.uimquestcape.states.barbassault;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for completing Wave 1 as a Defender in Barbarian Assault
 */
public class CompleteDefenderWave1State extends AbstractState {
    
    private boolean waveStarted = false;
    private boolean waveCompleted = false;
    
    public CompleteDefenderWave1State(UIMQuestCape script) {
        super(script, "CompleteDefenderWave1State");
    }
    
    @Override
    public int execute() {
        // Check if we're in the correct role room
        if (!BarbarianAssaultHelper.isInRoleRoom("defender")) {
            Logger.log("Not in Defender room, need to join first");
            return 1000;
        }
        
        // Handle game over or wave complete
        if (handleEndOfWaveInterfaces()) {
            return 600;
        }
        
        // Check if wave is completed
        if (waveCompleted) {
            Logger.log("Wave 1 completed as Defender");
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
        
        // Perform Defender role duties
        return performDefenderDuties();
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
    
    private int performDefenderDuties() {
        // Get the call (what food to use)
        String call = BarbarianAssaultHelper.getCallForRole("defender");
        
        // Bait trap based on call
        if (call.isEmpty()) {
            Logger.log("No call visible yet");
            return 600;
        }
        
        Logger.log("Defender call: " + call);
        
        // Check if we have bait
        if (!hasBait(call)) {
            // Get bait from dispenser
            getBaitFromDispenser(call);
            return 600;
        }
        
        // Find runner to bait
        NPC penanceRunner = NPCs.closest(npc -> 
            npc != null && 
            npc.getName() != null && 
            npc.getName().contains("Penance Runner") && 
            npc.hasAction("Use"));
        
        if (penanceRunner != null) {
            // Bait the trap
            GameObject trap = GameObjects.closest(obj -> 
                obj != null && 
                obj.getName() != null && 
                obj.getName().equals("Trap") && 
                obj.hasAction("Fix"));
            
            if (trap != null) {
                // Use bait on trap
                useBaitOnTrap(call, trap);
                return 600;
            }
        }
        
        return 600;
    }
    
    private boolean hasBait(String baitType) {
        return false; // Placeholder - would check inventory for bait
    }
    
    private void getBaitFromDispenser(String baitType) {
        // Find food dispenser
        GameObject dispenser = GameObjects.closest("Food dispenser");
        if (dispenser != null) {
            Logger.log("Getting " + baitType + " from dispenser");
            dispenser.interact("Take-" + baitType);
            Sleep.sleep(600, 1200);
        }
    }
    
    private void useBaitOnTrap(String baitType, GameObject trap) {
        Logger.log("Using " + baitType + " on trap");
        trap.interact("Fix");
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
        return BarbarianAssaultHelper.isInRoleRoom("defender") && !waveCompleted;
    }
}
