package es.noobcraft.oneblock.api.phases;

public interface SpecialActions {
    /**
     * Get the block where the action will be fired
     * @return the fired block
     */
    int getBlock();

    /**
     * Get the action that will be fired
     * @return action to fire
     */
    SpecialActions.Actions getAction();

    /**
     * Get the value for the action
     * @return action value
     */
    String getValue();

    /**
     * Enum with all the available actions
     */
    enum Actions {
        TITLE,
        MESSAGE,
        ACTIONBAR,
        BLOCK,
        MOB,
        LOOT_TABLE,
        UPGRADE
    }
}
