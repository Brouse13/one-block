package es.noobcraft.oneblock;

import es.noobcraft.oneblock.api.OneBlock;
import es.noobcraft.oneblock.api.database.ProfileLoader;
import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.world.WorldManager;
import es.noobcraft.oneblock.database.SqlProfileLoader;
import es.noobcraft.oneblock.player.SetPlayerCache;
import es.noobcraft.oneblock.world.OneBlockWorldManager;

public class API implements OneBlock {
    private final PlayerCache playerCache = new SetPlayerCache();
    private final ProfileLoader profileLoader = new SqlProfileLoader();
    private final WorldManager worldManager = new OneBlockWorldManager();

    @Override
    public PlayerCache getPlayerCache() {
        return playerCache;
    }

    @Override
    public ProfileLoader getProfileLoader() {
        return profileLoader;
    }

    @Override
    public WorldManager getWorldManager() {
        return worldManager;
    }
}
