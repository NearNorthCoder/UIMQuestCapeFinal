package org.dreambot.uimquestcape.util;

import org.dreambot.api.methods.quest.Quest;
import org.dreambot.api.methods.quest.Quests;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.utilities.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for accessing quest status and varbit information
 */
public class QuestVarbitManager {
    
    // Tutorial Island
    public static final int TUTORIAL_PROGRESS_VARBIT = 281;
    
    // Quest IDs
    public static final int COOKS_ASSISTANT_ID = 2;
    public static final int WITCHS_HOUSE_ID = 67;
    public static final int WATERFALL_QUEST_ID = 65;
    public static final int FIGHT_ARENA_ID = 17;
    public static final int TREE_GNOME_VILLAGE_ID = 62;
    public static final int LOST_CITY_ID = 31;
    public static final int FAIRY_TALE_PT1_ID = 16;
    public static final int FAIRY_TALE_PT2_ID = 15;
    
    // Quest Varbits
    public static final int RECIPE_FOR_DISASTER_VARBIT = 1850;
    public static final int FAIRY_TALE_PT2_VARBIT = 2328;
    public static final int ARDOUGNE_DIARY_VARBIT = 4458;
    public static final int STRONGHOLD_OF_SECURITY_VARBIT = 853;
    
    // Quest stages constants
    public static final int QUEST_NOT_STARTED = 0;
    public static final int QUEST_STARTED = 1;
    public static final int QUEST_COMPLETE = 2;
    
    // Cache to prevent repeated lookups
    private static final Map<Integer, Integer> varbitCache = new HashMap<>();
    private static final Map<String, Integer> questStatusCache = new HashMap<>();
    private static long lastCacheUpdate = 0;
    private static final long CACHE_TIMEOUT = 5000; // 5 seconds
    
    /**
     * Gets a varbit value from player settings
     * @param varbitId The varbit ID to check
     * @return The value of the varbit
     */
    public static int getVarbit(int varbitId) {
        // Update cache if needed
        updateCacheIfNeeded();
        
        // Use cached value if available
        if (varbitCache.containsKey(varbitId)) {
            return varbitCache.get(varbitId);
        }
        
        // Get and cache value
        int value = PlayerSettings.getBitValue(varbitId);
        varbitCache.put(varbitId, value);
        return value;
    }
    
    /**
     * Gets a config value from player settings
     * @param configId The config ID to check
     * @return The value of the config
     */
    public static int getConfig(int configId) {
        return PlayerSettings.getConfig(configId);
    }
    
    /**
     * Checks if a quest is completed based on its ID
     * @param questId The quest ID to check
     * @return true if the quest is completed
     */
    public static boolean isQuestCompleted(int questId) {
        return getQuestStatus(questId) == QUEST_COMPLETE;
    }
    
    /**
     * Gets the status of a quest (not started, started, or completed)
     * @param questId The quest ID to check
     * @return The quest status (0 = not started, 1 = started, 2 = completed)
     */
    public static int getQuestStatus(int questId) {
        // Update cache if needed
        updateCacheIfNeeded();
        
        String cacheKey = "quest_" + questId;
        if (questStatusCache.containsKey(cacheKey)) {
            return questStatusCache.get(cacheKey);
        }
        
        int status = QUEST_NOT_STARTED;
        
        // Try to get from Quests API first
        for (Quest quest : Quests.getAll()) {
            if (quest.getIndex() == questId) {
                if (quest.isFinished()) {
                    status = QUEST_COMPLETE;
                } else if (quest.getVarpValue() > 0) {
                    status = QUEST_STARTED;
                }
                break;
            }
        }
        
        // If not found in Quests API, use varbits/configs
        if (status == QUEST_NOT_STARTED) {
            switch (questId) {
                case COOKS_ASSISTANT_ID:
                    if (getVarbit(RECIPE_FOR_DISASTER_VARBIT) >= 1) {
                        status = QUEST_COMPLETE;
                    }
                    break;
                case WITCHS_HOUSE_ID:
                    int witchsHouseStatus = getConfig(226);
                    if (witchsHouseStatus >= 7) {
                        status = QUEST_COMPLETE;
                    } else if (witchsHouseStatus >= 1) {
                        status = QUEST_STARTED;
                    }
                    break;
                case WATERFALL_QUEST_ID:
                    int waterfallStatus = getConfig(65);
                    if (waterfallStatus >= 10) {
                        status = QUEST_COMPLETE;
                    } else if (waterfallStatus >= 1) {
                        status = QUEST_STARTED;
                    }
                    break;
                case FIGHT_ARENA_ID:
                    int fightArenaStatus = getConfig(17);
                    if (fightArenaStatus >= 15) {
                        status = QUEST_COMPLETE;
                    } else if (fightArenaStatus >= 1) {
                        status = QUEST_STARTED;
                    }
                    break;
                case TREE_GNOME_VILLAGE_ID:
                    int tgnStatus = getConfig(111);
                    if (tgnStatus >= 10) {
                        status = QUEST_COMPLETE;
                    } else if (tgnStatus >= 1) {
                        status = QUEST_STARTED;
                    }
                    break;
                case LOST_CITY_ID:
                    int lostCityStatus = getConfig(147);
                    if (lostCityStatus >= 6) {
                        status = QUEST_COMPLETE;
                    } else if (lostCityStatus >= 1) {
                        status = QUEST_STARTED;
                    }
                    break;
                case FAIRY_TALE_PT1_ID:
                    int ft1Status = getConfig(17);
                    if (ft1Status >= 3) {
                        status = QUEST_COMPLETE;
                    } else if (ft1Status >= 1) {
                        status = QUEST_STARTED;
                    }
                    break;
                case FAIRY_TALE_PT2_ID:
                    int ft2Status = getVarbit(FAIRY_TALE_PT2_VARBIT);
                    if (ft2Status >= 100) {
                        status = QUEST_COMPLETE;
                    } else if (ft2Status >= 1) {
                        status = QUEST_STARTED;
                    }
                    break;
            }
        }
        
        // Cache the result
        questStatusCache.put(cacheKey, status);
        return status;
    }
    
    /**
     * Gets the completion percentage of a quest
     * @param questId The quest ID to check
     * @return The completion percentage (0-100)
     */
    public static int getQuestCompletionPercentage(int questId) {
        int status = getQuestStatus(questId);
        
        if (status == QUEST_COMPLETE) {
            return 100;
        } else if (status == QUEST_NOT_STARTED) {
            return 0;
        }
        
        // For started quests, estimate progress based on configs/varbits
        switch (questId) {
            case WATERFALL_QUEST_ID:
                int waterfallStatus = getConfig(65);
                return (waterfallStatus * 100) / 10;
            case FIGHT_ARENA_ID:
                int fightArenaStatus = getConfig(17);
                return (fightArenaStatus * 100) / 15;
            case FAIRY_TALE_PT2_ID:
                int ft2Status = getVarbit(FAIRY_TALE_PT2_VARBIT);
                return ft2Status;
            default:
                return 50; // Default to 50% if started but can't determine
        }
    }
    
    /**
     * Gets the number of quest points the player has
     * @return The number of quest points
     */
    public static int getQuestPoints() {
        int points = 0;
        for (Quest quest : Quests.getAll()) {
            if (quest.isFinished()) {
                points += quest.getPointsReward();
            }
        }
        return points;
    }
    
    /**
     * Gets the total number of quests completed
     * @return The number of completed quests
     */
    public static int getCompletedQuestsCount() {
        int count = 0;
        for (Quest quest : Quests.getAll()) {
            if (quest.isFinished()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Update the cache if it's been too long since the last update
     */
    private static void updateCacheIfNeeded() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCacheUpdate > CACHE_TIMEOUT) {
            varbitCache.clear();
            questStatusCache.clear();
            lastCacheUpdate = currentTime;
        }
    }
}