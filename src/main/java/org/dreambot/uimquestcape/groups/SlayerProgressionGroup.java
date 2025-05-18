/**
 * FILENAME: SlayerProgressionGroup.java
 * Path: src/main/java/org/dreambot/uimquestcape/groups/SlayerProgressionGroup.java
 */

package org.dreambot.uimquestcape.groups;

import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.uimquestcape.State;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.states.slayer.*;
import org.dreambot.uimquestcape.util.QuestVarbitManager;
import org.dreambot.uimquestcape.util.StateGroup;

/**
 * Group for slayer progression and black mask/slayer helmet acquisition
 */
public class SlayerProgressionGroup extends StateGroup {
    
    // Constants for slayer progress
    private static final int SLAYER_HELMET_VARBIT = 4551;
    private static final int CURRENT_ASSIGNMENT_VARBIT = 394;
    private static final int ASSIGNED_AMOUNT_VARBIT = 395;
    private static final int REMAINING_AMOUNT_VARBIT = 396;
    
    public SlayerProgressionGroup(UIMQuestCape script) {
        super(script, "SlayerProgression");
        registerStates();
    }
    
    private void registerStates() {
        // Initial slayer training
        addState(new TravelToBurthorpeState(getScript()));
        addState(new GetTuraelAssignmentState(getScript()));
        addState(new CompleteEasyTasksState(getScript()));
        
        // Move to Vannaka
        addState(new TravelToEdgevilleState(getScript()));
        addState(new GetVannakaAssignmentState(getScript()));
        addState(new CompleteMediumTasksState(getScript()));
        
        // Move to Chaeldar
        addState(new TravelToZanarisState(getScript()));
        addState(new GetChaeldarAssignmentState(getScript()));
        addState(new CompleteCafeTasksState(getScript()));
        
        // Move to Nieve/Steve
        addState(new TravelToGnomeStrongholdState(getScript()));
        addState(new GetNieveAssignmentState(getScript()));
        addState(new CompleteHardTasksState(getScript()));
        
        // Move to Duradel
        addState(new TravelToShiloVillageState(getScript()));
        addState(new GetDuradelAssignmentState(getScript()));
        addState(new CompleteEliteTasksState(getScript()));
        
        // Black mask acquisition
        addState(new GetCaveHorrorsTaskState(getScript()));
        addState(new TravelToMosLeHarmlessState(getScript()));
        addState(new EnterCaveHorrorDungeonState(getScript()));
        addState(new KillCaveHorrorsState(getScript()));
        addState(new AcquireBlackMaskState(getScript()));
        
        // Slayer helmet creation
        addState(new GatherHelmetComponentsState(getScript()));
        addState(new CreateSlayerHelmetState(getScript()));
        
        // Link states in sequence
        linkStates();
    }
    
    private void linkStates() {
        // Initial slayer progression
        getStateByName("TravelToBurthorpeState").setNextState(getStateByName("GetTuraelAssignmentState"));
        getStateByName("GetTuraelAssignmentState").setNextState(getStateByName("CompleteEasyTasksState"));
        getStateByName("CompleteEasyTasksState").setNextState(getStateByName("TravelToEdgevilleState"));
        
        // Vannaka progression
        getStateByName("TravelToEdgevilleState").setNextState(getStateByName("GetVannakaAssignmentState"));
        getStateByName("GetVannakaAssignmentState").setNextState(getStateByName("CompleteMediumTasksState"));
        getStateByName("CompleteMediumTasksState").setNextState(getStateByName("TravelToZanarisState"));
        
        // Chaeldar progression
        getStateByName("TravelToZanarisState").setNextState(getStateByName("GetChaeldarAssignmentState"));
        getStateByName("GetChaeldarAssignmentState").setNextState(getStateByName("CompleteCafeTasksState"));
        getStateByName("CompleteCafeTasksState").setNextState(getStateByName("TravelToGnomeStrongholdState"));
        
        // Nieve progression
        getStateByName("TravelToGnomeStrongholdState").setNextState(getStateByName("GetNieveAssignmentState"));
        getStateByName("GetNieveAssignmentState").setNextState(getStateByName("CompleteHardTasksState"));
        getStateByName("CompleteHardTasksState").setNextState(getStateByName("TravelToShiloVillageState"));
        
        // Duradel progression
        getStateByName("TravelToShiloVillageState").setNextState(getStateByName("GetDuradelAssignmentState"));
        getStateByName("GetDuradelAssignmentState").setNextState(getStateByName("CompleteEliteTasksState"));
        getStateByName("CompleteEliteTasksState").setNextState(getStateByName("GetCaveHorrorsTaskState"));
        
        // Black mask acquisition
        getStateByName("GetCaveHorrorsTaskState").setNextState(getStateByName("TravelToMosLeHarmlessState"));
        getStateByName("TravelToMosLeHarmlessState").setNextState(getStateByName("EnterCaveHorrorDungeonState"));
        getStateByName("EnterCaveHorrorDungeonState").setNextState(getStateByName("KillCaveHorrorsState"));
        getStateByName("KillCaveHorrorsState").setNextState(getStateByName("AcquireBlackMaskState"));
        getStateByName("AcquireBlackMaskState").setNextState(getStateByName("GatherHelmetComponentsState"));
        
        // Slayer helmet creation
        getStateByName("GatherHelmetComponentsState").setNextState(getStateByName("CreateSlayerHelmetState"));
    }
    
    @Override
    public State getInitialState() {
        return getStateByName("TravelToBurthorpeState");
    }
    
    @Override
    public boolean requirementsMet() {
        // Check if combat equipment is acquired
        return Inventory.contains("Dragon scimitar") && 
               Inventory.contains("Fighter torso") &&
               Inventory.contains("Rune crossbow");
    }
    
    @Override
    public State determineCurrentState() {
        // Check slayer progress
        int slayerLevel = Skills.getRealLevel(Skills.SLAYER);
        boolean hasBlackMask = Inventory.contains("Black mask");
        boolean hasSlayerHelmet = Inventory.contains("Slayer helmet") || 
                                 QuestVarbitManager.getVarbit(SLAYER_HELMET_VARBIT) == 1;
        int combatLevel = getCombatLevel();
        
        // Determine current state based on progress
        if (hasSlayerHelmet) {
            markCompleted();
            return null; // Slayer helmet acquired, group complete
        } else if (hasBlackMask) {
            return getStateByName("GatherHelmetComponentsState");
        } else if (slayerLevel >= 58) {
            return getStateByName("GetCaveHorrorsTaskState");
        } else if (combatLevel >= 100) {
            return getStateByName("TravelToShiloVillageState");
        } else if (combatLevel >= 85) {
            return getStateByName("TravelToGnomeStrongholdState");
        } else if (combatLevel >= 75) {
            return getStateByName("TravelToZanarisState");
        } else if (combatLevel >= 40) {
            return getStateByName("TravelToEdgevilleState");
        } else {
            return getStateByName("TravelToBurthorpeState");
        }
    }
    
    // Helper method to calculate combat level
    private int getCombatLevel() {
        int attack = Skills.getRealLevel(Skills.ATTACK);
        int strength = Skills.getRealLevel(Skills.STRENGTH);
        int defence = Skills.getRealLevel(Skills.DEFENCE);
        int hitpoints = Skills.getRealLevel(Skills.HITPOINTS);
        int prayer = Skills.getRealLevel(Skills.PRAYER);
        int ranged = Skills.getRealLevel(Skills.RANGED);
        int magic = Skills.getRealLevel(Skills.MAGIC);
        
        // Simple combat level formula
        double baseLevel = (defence + hitpoints + Math.floor(prayer/2)) * 0.25;
        double meleeCombat = (attack + strength) * 0.325;
        double rangeCombat = Math.floor(ranged * 1.5) * 0.325;
        double magicCombat = Math.floor(magic * 1.5) * 0.325;
        
        double combatLevel = baseLevel + Math.max(meleeCombat, Math.max(rangeCombat, magicCombat));
        
        return (int) combatLevel;
    }
}