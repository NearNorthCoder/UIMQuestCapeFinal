/**
 * FILENAME: JadBossState.java
 * Path: src/main/java/org/dreambot/uimquestcape/states/boss/JadBossState.java
 */

package org.dreambot.uimquestcape.states.boss;

import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * Implementation for Fire Cape (Jad) fight
 */
public class JadBossState extends BossState {
    
    // Constants
    private static final String JAD_NAME = "TzTok-Jad";
    private static final int JAD_ID = 3127;
    private static final int HEALER_ID = 3128;
    
    // Wave tracking
    private int currentWave = 1;
    private static final int TOTAL_WAVES = 63; // 62 regular waves + Jad
    
    // Areas and tiles
    private final Area fightCavesArea = new Area(2368, 5127, 2430, 5183);
    private final Tile centerTile = new Tile(2399, 5155, 0);
    private final Tile safespotTile = new Tile(2399, 5155, 0); // Example safespot
    
    // Prayer switching
    private Prayer currentPrayer = Prayer.PROTECT_FROM_MAGIC;
    
    // Healer management
    private int healersTagged = 0;
    
    public JadBossState(UIMQuestCape script, String name) {
        super(script, name);
        this.eatAtHealthPercent = 55; // Higher threshold for Fight Caves
    }
    
    @Override
    protected void setupForBossFight() {
        // Enable protect from melee for most waves
        if (!Prayers.isActive(Prayer.PROTECT_FROM_MELEE)) {
            Prayers.toggle(Prayer.PROTECT_FROM_MELEE, true);
        }
        
        // Enable Eagle Eye or Rigour if available
        if (Prayers.getPoints() > 40) {
            if (Prayers.canUse(Prayer.RIGOUR)) {
                Prayers.toggle(Prayer.RIGOUR, true);
            } else if (Prayers.canUse(Prayer.EAGLE_EYE)) {
                Prayers.toggle(Prayer.EAGLE_EYE, true);
            }
        }
    }
    
    @Override
    protected int handlePreparationPhase() {
        // Check if we're in the Fight Caves
        if (!fightCavesArea.contains(Players.getLocal())) {
            Logger.log("Not in Fight Caves, traveling there");
            // Logic to travel to TzHaar Fight Caves would go here
            return 1000;
        }
        
        // Move to center/safe spot if not already there
        if (Players.getLocal().distance(centerTile) > 5) {
            Walking.walk(centerTile);
            return 1000;
        }
        
        // Check if waves have started
        if (areEnemiesPresent()) {
            Logger.log("Fight Caves started");
            advancePhase(); // Move to combat phase
            return 600;
        }
        
        // Interact with TzHaar-Mej-Jal to start
        if (startFightCaves()) {
            Logger.log("Starting Fight Caves");
            Sleep.sleepUntil(() -> areEnemiesPresent(), 10000);
            return 1000;
        }
        
        return 600;
    }
    
    @Override
    protected int handleCombatPhase() {
        // Check if any enemies are present
        if (!areEnemiesPresent()) {
            // If no enemies, we either completed a wave or the caves
            handleWaveCompletion();
            return 1000;
        }
        
        // Check if Jad has spawned (wave 63)
        NPC jad = NPCs.closest(JAD_NAME);
        if (jad != null) {
            Logger.log("Jad has spawned! Moving to Jad phase");
            advancePhase(); // Move to special attack phase for Jad
            return 600;
        }
        
        // Handle normal waves
        return handleNormalWaves();
    }
    
    @Override
    protected int handleSpecialAttackPhase() {
        // This phase handles the Jad fight specifically
        NPC jad = NPCs.closest(JAD_NAME);
        if (jad == null) {
            Logger.log("Cannot find Jad, checking if defeated");
            return handlePossibleVictory();
        }
        
        // Move to safe spot if one is available
        if (Players.getLocal().distance(safespotTile) > 1) {
            Walking.walk(safespotTile);
            return 600;
        }
        
        // Prayer switching based on Jad's animation
        handleJadPrayerSwitching(jad);
        
        // Handle healers if Jad is below 50% health
        if (jad.getHealthPercent() < 50 && healersTagged < 4) {
            handleHealers();
            return 600;
        }
        
        // Attack Jad if not already attacking and prayers are correct
        if (!Players.getLocal().isAnimating() && !Players.getLocal().isMoving()) {
            if (jad.interact("Attack")) {
                Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 2000);
            }
        }
        
        return 300; // Faster check rate for prayer switching
    }
    
    private int handlePossibleVictory() {
        // Check if fight is over and we won
        if (NPCs.closest(JAD_NAME) == null && !areEnemiesPresent() && Players.getLocal().getHealthPercent() > 0) {
            Logger.log("Jad appears to be defeated!");
            Sleep.sleep(2000, 3000); // Wait for Fire Cape
            advancePhase(); // Move to victory phase
            return 1000;
        }
        return 600;
    }
    
    private boolean areEnemiesPresent() {
        // Check if any Fight Caves NPCs are present
        return NPCs.closest(npc -> 
            npc != null && 
            npc.getName() != null &&
            npc.getName().contains("Tz")
        ) != null;
    }
    
    private void handleWaveCompletion() {
        // If we've completed a wave, increment the counter
        currentWave++;
        Logger.log("Completed wave " + (currentWave - 1) + ", now on wave " + currentWave);
        
        // If we've completed all waves, move to victory phase
        if (currentWave > TOTAL_WAVES) {
            Logger.log("Completed all waves!");
            advancePhase(); // Move to victory phase
        }
        
        // Reset healers tagged if we're on wave 63 (Jad)
        if (currentWave == 63) {
            healersTagged = 0;
        }
    }
    
    private int handleNormalWaves() {
        // Set appropriate prayer based on current enemies
        setPriorityPrayer();
        
        // Find highest priority target to attack
        NPC target = findHighestPriorityTarget();
        if (target != null && !Players.getLocal().isInCombat()) {
            target.interact("Attack");
            Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 2000);
        }
        
        // Drink prayer potion if needed
        if (Prayers.getPoints() < 20) {
            drinkPrayerPotion();
        }
        
        return 600;
    }
    
    private void setPriorityPrayer() {
        // Determine which protection prayer to use based on enemies present
        
        // Check for Ket-Zek (level 360, magic attack)
        if (NPCs.closest("TzTok-Jad") != null) {
            // For Jad, use handleJadPrayerSwitching method instead
            return;
        } else if (NPCs.closest("Ket-Zek") != null) {
            if (!Prayers.isActive(Prayer.PROTECT_FROM_MAGIC)) {
                Prayers.toggle(Prayer.PROTECT_FROM_MELEE, false);
                Prayers.toggle(Prayer.PROTECT_FROM_MISSILES, false);
                Prayers.toggle(Prayer.PROTECT_FROM_MAGIC, true);
            }
        } 
        // Check for Tok-Xil (level 90, ranged attack)
        else if (NPCs.closest("Tok-Xil") != null) {
            if (!Prayers.isActive(Prayer.PROTECT_FROM_MISSILES)) {
                Prayers.toggle(Prayer.PROTECT_FROM_MELEE, false);
                Prayers.toggle(Prayer.PROTECT_FROM_MAGIC, false);
                Prayers.toggle(Prayer.PROTECT_FROM_MISSILES, true);
            }
        }
        // Default to protect from melee for lower waves
        else if (!Prayers.isActive(Prayer.PROTECT_FROM_MELEE)) {
            Prayers.toggle(Prayer.PROTECT_FROM_MAGIC, false);
            Prayers.toggle(Prayer.PROTECT_FROM_MISSILES, false);
            Prayers.toggle(Prayer.PROTECT_FROM_MELEE, true);
        }
    }
    
    private NPC findHighestPriorityTarget() {
        // Priority order (highest to lowest)
        String[] priorities = {
            "Ket-Zek",    // Level 360 (Mage)
            "Tok-Xil",    // Level 90 (Ranger)
            "Tz-Kek",     // Level 45 (Melee)
            "Tz-Kih"      // Level 22 (small melee/magic)
        };
        
        for (String npcName : priorities) {
            NPC target = NPCs.closest(npcName);
            if (target != null) {
                return target;
            }
        }
        
        // If no priority targets found, get any monster
        return NPCs.closest(npc -> 
            npc != null && 
            npc.getName() != null &&
            npc.getName().contains("Tz")
        );
    }
    
    private void handleJadPrayerSwitching(NPC jad) {
        int animation = jad.getAnimation();
        
        // Animation 2656 = mage attack, 2652 = range attack
        if (animation == 2656) { // Magic attack
            if (currentPrayer != Prayer.PROTECT_FROM_MAGIC) {
                Logger.log("Switching to Protect from Magic");
                Prayers.toggle(currentPrayer, false);
                Prayers.toggle(Prayer.PROTECT_FROM_MAGIC, true);
                currentPrayer = Prayer.PROTECT_FROM_MAGIC;
            }
        } else if (animation == 2652) { // Ranged attack
            if (currentPrayer != Prayer.PROTECT_FROM_MISSILES) {
                Logger.log("Switching to Protect from Missiles");
                Prayers.toggle(currentPrayer, false);
                Prayers.toggle(Prayer.PROTECT_FROM_MISSILES, true);
                currentPrayer = Prayer.PROTECT_FROM_MISSILES;
            }
        }
    }
    
    private void handleHealers() {
        // Find and tag healers
        NPC healer = NPCs.closest(HEALER_ID);
        if (healer != null) {
            Logger.log("Tagging healer");
            if (healer.interact("Attack")) {
                healersTagged++;
                Sleep.sleep(300, 600);
                
                // Switch back to Jad after tagging
                NPC jad = NPCs.closest(JAD_NAME);
                if (jad != null) {
                    jad.interact("Attack");
                }
            }
        }
    }
    
    private boolean startFightCaves() {
        // Interact with TzHaar-Mej-Jal to start the Fight Caves
        NPC starter = NPCs.closest("TzHaar-Mej-Jal");
        if (starter != null) {
            return starter.interact("Talk-to");
        }
        return false;
    }
    
    private void drinkPrayerPotion() {
        // Logic to drink a prayer potion
        Logger.log("Drinking prayer potion");
        // Placeholder
    }
}