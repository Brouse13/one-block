package es.noobcraft.oneblock.api.scoreboard;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;

public interface ScoreboardManager {
    /**
     * Get the ScoreBoard from the specific player
     * @param player player to search scoreboard.
     * @return player OneBlockScoreboard
     */
    OneBlockScoreBoard getScoreBoard(OneBlockPlayer player);

    /**
     * Create a new OneBlockScoreboard to the player, this
     * method will overwrite the actual player scoreboard.
     * @param player player to create scoreboard
     * @param scoreBoard scoreboard to create
     * @return the created Scoreboard
     */
    OneBlockScoreBoard createScoreboard(OneBlockPlayer player, OneBlockScoreBoard scoreBoard);

    /**
     * Remove the current OneBlockScoreboard from the
     * given player.
     * @param player player to remove scoreboard
     * @return the operation status
     */
    boolean removeScoreBoard(OneBlockPlayer player);

    /**
     * Clear all the player scoreboards.
     */
    void clearScoreBoards();

    /**
     * Execute OneBlockScoreboard#update() to update
     * all the loaded scoreboards.
     */
    void update();
}
