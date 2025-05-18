package org.dreambot.api.methods.quest;

import java.util.ArrayList;
import java.util.List;

/**
 * Stub implementation of the Quests utility class for DreamBot API
 */
public class Quests {
    private static final List<Quest> quests = new ArrayList<>();

    static {
        // Initialize with some common quests
        quests.add(new Quest(2, "Cook's Assistant", 1));
        quests.add(new Quest(15, "Fairy Tale II - Cure a Queen", 2));
        quests.add(new Quest(16, "Fairy Tale I - Growing Pains", 2));
        quests.add(new Quest(17, "Fight Arena", 2));
        quests.add(new Quest(20, "Witch's House", 4));
        quests.add(new Quest(31, "Lost City", 3));
        quests.add(new Quest(35, "Monkey Madness I", 3));
        quests.add(new Quest(53, "Spirits of the Elid", 2));
        quests.add(new Quest(60, "The Feud", 1));
        quests.add(new Quest(62, "Tree Gnome Village", 2));
        quests.add(new Quest(65, "Waterfall Quest", 1));
        quests.add(new Quest(67, "Witch's House", 4));
    }

    /**
     * Gets all available quests
     * @return List of all quests
     */
    public static List<Quest> getAll() {
        return quests;
    }

    /**
     * Gets a quest by ID
     * @param id Quest ID
     * @return Quest object or null if not found
     */
    public static Quest getQuest(int id) {
        for (Quest quest : quests) {
            if (quest.getIndex() == id) {
                return quest;
            }
        }
        return null;
    }
}