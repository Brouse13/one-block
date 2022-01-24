package es.noobcraft.oneblock;

import es.noobcraft.oneblock.api.OneBlock;
import es.noobcraft.oneblock.api.flags.IslandPermissionManager;
import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.profile.ProfileCache;
import es.noobcraft.oneblock.api.profile.ProfileLoader;
import es.noobcraft.oneblock.api.profile.ProfileManager;
import es.noobcraft.oneblock.api.scoreboard.ScoreboardManager;
import es.noobcraft.oneblock.api.world.WorldManager;
import es.noobcraft.oneblock.flag.SetIslandPermissionManager;
import es.noobcraft.oneblock.player.SetPlayerCache;
import es.noobcraft.oneblock.profile.BaseProfileManager;
import es.noobcraft.oneblock.profile.SetProfileCache;
import es.noobcraft.oneblock.profile.SqlProfileLoader;
import es.noobcraft.oneblock.scoreboard.BaseScoreboardManager;
import es.noobcraft.oneblock.world.OneBlockWorldManager;

public class API implements OneBlock {
    private final PlayerCache playerCache = new SetPlayerCache();
    private final ProfileCache profileCache = new SetProfileCache();
    private final ProfileLoader profileLoader = new SqlProfileLoader();
    private final IslandPermissionManager islandPermissionsManager = new SetIslandPermissionManager();
    private final ProfileManager profileManager = new BaseProfileManager();
    private final WorldManager worldManager = new OneBlockWorldManager();
    private final ScoreboardManager scoreboardManager = new BaseScoreboardManager();

    @Override
    public ProfileLoader getProfileLoader() {
        return profileLoader;
    }

    @Override
    public IslandPermissionManager getIslandPermissionManager() {
        return islandPermissionsManager;
    }

    @Override
    public PlayerCache getPlayerCache() {
        return playerCache;
    }

    @Override
    public ProfileCache getProfileCache() {
        return profileCache;
    }

    @Override
    public WorldManager getWorldManager() {
        return worldManager;
    }

    @Override
    public ProfileManager getProfileManager() {
        return profileManager;
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}
