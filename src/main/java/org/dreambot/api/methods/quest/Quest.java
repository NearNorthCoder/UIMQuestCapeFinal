package org.dreambot.api.methods.quest;

/**
 * Represents an OSRS quest
 */
public class Quest {
    private final int index;
    private final String name;
    private final int difficulty;

    /**
     * Creates a new Quest object
     * @param index The quest ID
     * @param name The quest name
     * @param difficulty The quest difficulty (1-5)
     */
    public Quest(int index, String name, int difficulty) {
        this.index = index;
        this.name = name;
        this.difficulty = difficulty;
    }

    /**
     * Gets the quest ID
     * @return Quest ID
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the quest name
     * @return Quest name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the quest difficulty
     * @return Quest difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }
}