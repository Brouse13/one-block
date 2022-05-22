package es.noobcraft.oneblock.scoreboard;

import com.google.common.collect.Maps;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.scoreboard.OneBlockScoreBoard;

import java.util.Map;

public class BaseScoreboardManager {
    private static final Map<OneBlockPlayer, OneBlockScoreBoard> scoreboards = Maps.newHashMap();

    public static void addScoreboard(OneBlockPlayer player, OneBlockScoreBoard scoreBoard) {
        scoreboards.put(player, scoreBoard);
    }

    public static void removeScoreBoard(OneBlockPlayer player) {
        scoreboards.remove(player);
    }

    public static void clearScoreBoards() {
        scoreboards.clear();
        Logger.log(LoggerType.CONSOLE, "Scoreboards cleared");
    }

    public static void update() {
        scoreboards.values().forEach(OneBlockScoreBoard::update);
    }
}
