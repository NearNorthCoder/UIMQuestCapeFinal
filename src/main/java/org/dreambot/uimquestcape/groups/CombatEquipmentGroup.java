/**
 * FILENAME: CombatEquipmentGroup.java
 * Path: src/main/java/org/dreambot/uimquestcape/groups/CombatEquipmentGroup.java
 */

package org.dreambot.uimquestcape.groups;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.uimquestcape.State;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.states.barbassault.*;
import org.dreambot.uimquestcape.states.quests.monkeymadness.*;
import org.dreambot.uimquestcape.util.QuestVarbitManager;
import org.dreambot.uimquestcape.util.StateGroup;

/**
 * Group for mid-game combat equipment acquisition
 */
public class CombatEquipmentGroup extends StateGroup {
    
    // Constants for quest completion status
    private static final int MONKEY_MADNESS_QUEST_ID = 35;
    private static final int FIGHTER_TORSO_VARBIT = 3784;
    private static final int RUNE_CROSSBOW_ACQUIRED = 1;
    
    public CombatEquipmentGroup(UIMQuestCape script) {
        super(script, "CombatEquipment");
        registerStates();
    }
    
    private void registerStates() {
        // Fighter Torso acquisition
        addState(new TravelToBarbarianAssaultState(getScript()));
        addState(new JoinAttackerRoleState(getScript()));
        addState(new CompleteAttackerWave1State(getScript()));
        addState(new JoinDefenderRoleState(getScript()));
        addState(new CompleteDefenderWave1State(getScript()));
        addState(new JoinHealerRoleState(getScript()));
        addState(new CompleteHealerWave1State(getScript()));
        addState(new JoinCollectorRoleState(getScript()));
        addState(new CompleteCollectorWave1State(getScript()));
        
        // Complete later waves
        addState(new CompleteAllRolesWave2State(getScript()));
        addState(new CompleteAllRolesWave3State(getScript()));
        addState(new CompleteAllRolesWave4State(getScript()));
        addState(new CompleteAllRolesWave5State(getScript()));
        addState(new CompleteAllRolesWave6State(getScript()));
        addState(new CompleteAllRolesWave7State(getScript()));
        addState(new CompleteAllRolesWave8State(getScript()));
        addState(new CompleteAllRolesWave9State(getScript()));
        addState(new CompleteAllRolesWave10State(getScript()));
        
        // Purchase Fighter Torso
        addState(new PurchaseFighterTorsoState(getScript()));
        
        // Dragon Scimitar acquisition (Monkey Madness)
        addState(new MonkeyMadnessStartState(getScript()));
        addState(new TravelToApeAtollState(getScript()));
        addState(new GetMonkeyAmuletMouldState(getScript()));
        addState(new ReturnToGnomeStrongholdState(getScript()));
        addState(new CreateEnchantedBarState(getScript()));
        addState(new CreateMonkeyTalismanState(getScript()));
        addState(new ReturnToApeAtollState(getScript()));
        addState(new GetMonkeyGreegreeState(getScript()));
        addState(new EnterTempleState(getScript()));
        addState(new SpeakToMonkeyElderState(getScript()));
        addState(new SpeakToKingAwowogeiState(getScript()));
        addState(new ReturnToNarnodeState(getScript()));
        addState(new NavigateCrashSiteCavernState(getScript()));
        addState(new CompletePlatformPuzzleState(getScript()));
        addState(new DefeatJungleDemonState(getScript()));
        addState(new MonkeyMadnessCompleteState(getScript()));
        addState(new PurchaseDragonScimitarState(getScript()));
        
        // Rune Crossbow acquisition
        addState(new TravelToWildernessState(getScript()));
        addState(new DefeatCrazyArchaeologistState(getScript()));
        addState(new AcquireRuneCrossbowState(getScript()));
        
        // Link states in sequence
        linkStates();
    }
    
    private void linkStates() {
        // Fighter Torso sequence
        getStateByName("TravelToBarbarianAssaultState").setNextState(getStateByName("JoinAttackerRoleState"));
        getStateByName("JoinAttackerRoleState").setNextState(getStateByName("CompleteAttackerWave1State"));
        // ... link remaining BA wave states
        getStateByName("CompleteAllRolesWave10State").setNextState(getStateByName("PurchaseFighterTorsoState"));
        getStateByName("PurchaseFighterTorsoState").setNextState(getStateByName("MonkeyMadnessStartState"));
        
        // Monkey Madness sequence
        getStateByName("MonkeyMadnessStartState").setNextState(getStateByName("TravelToApeAtollState"));
        // ... link remaining MM states
        getStateByName("MonkeyMadnessCompleteState").setNextState(getStateByName("PurchaseDragonScimitarState"));
        getStateByName("PurchaseDragonScimitarState").setNextState(getStateByName("TravelToWildernessState"));
        
        // Rune Crossbow sequence
        getStateByName("TravelToWildernessState").setNextState(getStateByName("DefeatCrazyArchaeologistState"));
        getStateByName("DefeatCrazyArchaeologistState").setNextState(getStateByName("AcquireRuneCrossbowState"));
    }
    
    @Override
    public State getInitialState() {
        return getStateByName("TravelToBarbarianAssaultState");
    }
    
    @Override
    public boolean requirementsMet() {
        // Check if Desert Treasure is completed
        return QuestVarbitManager.getVarbit(DESERT_TREASURE_VARBIT) >= 100;
    }
    
    @Override
    public State determineCurrentState() {
        // Check equipment status
        boolean hasFighterTorso = Equipment.contains("Fighter torso");
        boolean hasDragonScimitar = Equipment.contains("Dragon scimitar") || Inventory.contains("Dragon scimitar");
        boolean hasRuneCrossbow = Equipment.contains("Rune crossbow") || Inventory.contains("Rune crossbow");
        
        // Check quest completion status
        boolean monkeyMadnessCompleted = QuestVarbitManager.isQuestCompleted(MONKEY_MADNESS_QUEST_ID);
        
        // Determine current state based on progress
        if (hasRuneCrossbow) {
            markCompleted();
            return null; // All equipment acquired, group complete
        } else if (hasDragonScimitar) {
            return getStateByName("TravelToWildernessState");
        } else if (monkeyMadnessCompleted) {
            return getStateByName("PurchaseDragonScimitarState");
        } else if (hasFighterTorso) {
            return getStateByName("MonkeyMadnessStartState");
        } else {
            // Check BA progress using honor points varbit
            int baProgress = QuestVarbitManager.getVarbit(FIGHTER_TORSO_VARBIT);
            if (baProgress >= 375) {
                return getStateByName("PurchaseFighterTorsoState");
            } else {
                // Determine which wave and role to focus on next
                // This would need more logic based on specific BA tracking varbits
                return getStateByName("TravelToBarbarianAssaultState");
            }
        }
    }
}