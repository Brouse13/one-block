package es.noobcraft.oneblock.scoreboard;

import com.google.common.collect.Maps;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.scoreboard.OneBlockScoreBoard;
import es.noobcraft.oneblock.api.scoreboard.ScoreboardManager;
import es.noobcraft.oneblock.logger.Logger;
import es.noobcraft.oneblock.logger.LoggerType;

import java.util.Map;

public class BaseScoreboardManager implements ScoreboardManager {
    Map<OneBlockPlayer, OneBlockScoreBoard> scoreboards = Maps.newHashMap();

    @Override
    public OneBlockScoreBoard getScoreBoard(OneBlockPlayer player) {
        return scoreboards.get(player);
    }

    @Override
    public OneBlockScoreBoard createScoreboard(OneBlockPlayer player, OneBlockScoreBoard scoreBoard) {
        return scoreboards.put(player, scoreBoard);
    }

    @Override
    public boolean removeScoreBoard(OneBlockPlayer player) {
        return scoreboards.remove(player) == null;
    }

    @Override
    public void clearScoreBoards() {
        scoreboards.clear();
        Logger.log(LoggerType.CONSOLE, "Scoreboards cleared");
    }

    @Override
    public void update() {
        scoreboards.values().forEach(OneBlockScoreBoard::update);
    }
}
