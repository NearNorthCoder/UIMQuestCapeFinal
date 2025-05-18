package org.dreambot.uimquestcape.states.barbassault;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for completing Wave 2 as all roles in Barbarian Assault
 */
public class CompleteAllRolesWave2State extends AbstractState {

    private String currentRole = "attacker";
    private boolean waveStarted = false;
    private boolean waveCompleted = false;
    private int completedRoles = 0;
    
    public CompleteAllRolesWave2State(UIMQuestCape script) {
        super(script, "CompleteAllRolesWave2State");
    }
    
    @Override
    public int execute() {
        // Check if all roles completed
        if (completedRoles >= 4) {
            Logger.log("All roles completed for Wave 2");
            complete();
            return 600;
        }
        
        // Check if we've completed the current role
        if (waveCompleted) {
            completedRoles++;
            waveCompleted = false;
            waveStarted = false;
            
            // Move to next role
            switch (currentRole) {
                case "attacker":
                    currentRole = "defender";
                    return joinNewRole("defender");
                case "defender":
                    currentRole = "collector";
                    return joinNewRole("collector");
                case "collector":
                    currentRole = "healer";
                    return joinNewRole("healer");
                case "healer":
                    complete();
                    return 600;
            }
        }
        
        // Check if we're in the right role room
        if (!BarbarianAssaultHelper.isInRoleRoom(currentRole)) {
            return joinNewRole(currentRole);
        }
        
        // Handle game over or wave complete
        if (handleEndOfWaveInterfaces()) {
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
        
        // Perform role-specific duties
        switch (currentRole) {
            case "attacker":
                return performAttackerDuties();
            case "defender":
                return performDefenderDuties();
            case "collector":
                return performCollectorDuties();
            case "healer":
                return performHealerDuties();
            default:
                return 600;
        }
    }
    
    private int joinNewRole(String role) {
        Logger.log("Joining " + role + " role for Wave 2");
        
        // First exit room if in another role
        if (BarbarianAssaultHelper.isInRoleRoom("attacker") ||
            BarbarianAssaultHelper.isInRoleRoom("defender") ||
            BarbarianAssaultHelper.isInRoleRoom("collector") ||
            BarbarianAssaultHelper.isInRoleRoom("healer")) {
            // Find exit ladder
            Logger.log("Exiting current role room");
            // Exit room logic would go here
            return 1000;
        }
        
        // If in lobby, join new role
        if (BarbarianAssaultHelper.isInLobby()) {
            if (BarbarianAssaultHelper.enterRoleRoom(role)) {
                Logger.log("Entered " + role + " room");
                return 1000;
            }
        }
        
        return 600;
    }
    
    private int handleTeamFormation() {
        // Check if enough teammates have joined
        if (BarbarianAssaultHelper.hasTeamFormedForWave()) {
            Logger.log("Team has formed for Wave 2, starting as " + currentRole);
            waveStarted = true;
            return 600;
        }
        
        Logger.log("Waiting for team to form");
        return 1000;
    }
    
    // Role-specific duty methods similar to Wave 1 implementations
    private int performAttackerDuties() {
        // Logic for attacker duties - similar to Wave 1 but with higher difficulty
        Logger.log("Performing attacker duties for Wave 2");
        return 600;
    }
    
    private int performDefenderDuties() {
        // Logic for defender duties - similar to Wave 1 but with higher difficulty
        Logger.log("Performing defender duties for Wave 2");
        return 600;
    }
    
    private int performCollectorDuties() {
        // Logic for collector duties - similar to Wave 1 but with higher difficulty
        Logger.log("Performing collector duties for Wave 2");
        return 600;
    }
    
    private int performHealerDuties() {
        // Logic for healer duties - similar to Wave 1 but with higher difficulty
        Logger.log("Performing healer duties for Wave 2");
        return 600;
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
            Logger.log("Wave 2 completed as " + currentRole);
            waveCompleted = true;
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean canExecute() {
        return completedRoles < 4;
    }
}
