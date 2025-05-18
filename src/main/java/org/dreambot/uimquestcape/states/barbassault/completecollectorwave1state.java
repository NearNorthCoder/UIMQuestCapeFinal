package org.dreambot.uimquestcape.states.barbassault;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for completing Wave 1 as a Collector in Barbarian Assault
 */
public class CompleteCollectorWave1State extends AbstractState {
    
    private boolean waveStarted = false;
    private boolean waveCompleted = false;
    
    public CompleteCollectorWave1State(UIMQuestCape script) {
        super(script, "CompleteCollectorWave1State");
    }
    
    @Override
    public int execute() {
        // Check if we're in the correct role room
        if (!BarbarianAssaultHelper.isInRoleRoom("collector")) {
            Logger.log("Not in Collector room, need to join first");
            return 1000;
        }
        
        // Handle game over or wave complete
        if (handleEndOfWaveInterfaces()) {
            return 600;
        }
        
        // Check if wave is completed
        if (waveCompleted) {
            Logger.log("Wave 1 completed as Collector");
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
        
        // Perform Collector role duties
        return performCollectorDuties();
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
    
    private int performCollectorDuties() {
        // Get the call (what eggs to collect)
        String call = BarbarianAssaultHelper.getCallForRole("collector");
        
        // Collect correct colored eggs based on call
        if (call.isEmpty()) {
            Logger.log("No call visible yet");
            return 600;
        }
        
        Logger.log("Collector call: " + call);
        
        // Find eggs of the correct color
        GameObject egg = GameObjects.closest(obj -> 
            obj != null && 
            obj.getName() != null && 
            obj.getName().contains(call + " egg") && 
            obj.hasAction("Take"));
        
        if (egg != null) {
            Logger.log("Collecting " + call + " egg");
            egg.interact("Take");
            Sleep.sleep(600, 1200);
            return 600;
        }
        
        // Deposit eggs in cannon if we have any
        if (hasEggs()) {
            depositEggsInCannon();
            return 600;
        }
        
        return 600;
    }
    
    private boolean hasEggs() {
        // Check if we have eggs in inventory
        return false; // Placeholder - would check inventory for eggs
    }
    
    private void depositEggsInCannon() {
        // Find egg launcher/cannon
        GameObject cannon = GameObjects.closest("Egg launcher");
        if (cannon != null) {
            Logger.log("Depositing eggs in cannon");
            cannon.interact("Stock-up");
            Sleep.sleep(600, 1200);
        }
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
        return BarbarianAssaultHelper.isInRoleRoom("collector") && !waveCompleted;
    }
}
