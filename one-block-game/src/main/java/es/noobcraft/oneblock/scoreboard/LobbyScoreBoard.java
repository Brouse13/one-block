package es.noobcraft.oneblock.scoreboard;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.core.api.scoreboard.ScoreBoard;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.scoreboard.OneBlockScoreBoard;

public class LobbyScoreBoard implements OneBlockScoreBoard {
    private final Translator translator = Core.getTranslator();

    private final NoobPlayer noobPlayer;
    private ScoreBoard scoreBoard;
    private final OneBlockPlayer oneBlockPlayer;

    public LobbyScoreBoard(OneBlockPlayer oneBlockPlayer) {
        this.noobPlayer = Core.getPlayerCache().getPlayer(oneBlockPlayer.getName());
        this.oneBlockPlayer = oneBlockPlayer;

        for (ScoreBoard loadedScoreboard : SpigotCore.getScoreBoardManager().getScoreBoards()) {
            if (loadedScoreboard.getPlayer().getUsername().equals(noobPlayer.getUsername()))
                this.scoreBoard = loadedScoreboard;
        }

        if (scoreBoard == null) this.scoreBoard = SpigotCore.getScoreBoardManager().createScoreBoard(noobPlayer);
    }

    @Override
    public void update() {
        scoreBoard.setTitle(translator.getLegacyText(noobPlayer, "one-block.scoreboard.lobby.title"));
        scoreBoard.set(translator.getLegacyTextList(noobPlayer, "one-block.scoreboard.lobby.content",
                oneBlockPlayer.getName(), oneBlockPlayer.getProfiles().size()));
    }
}
