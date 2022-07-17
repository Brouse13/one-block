package es.noobcraft.oneblock;

import es.noobcraft.oneblock.api.OneBlock;
import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.profile.ProfileCache;
import es.noobcraft.oneblock.api.world.WorldManager;
import es.noobcraft.oneblock.player.SetOneBlockPlayerCache;
import es.noobcraft.oneblock.profile.MapProfileCache;
import es.noobcraft.oneblock.world.OneBlockWorldManager;

public class API implements OneBlock {
    private final PlayerCache playerCache = new SetOneBlockPlayerCache();
    private final ProfileCache profileCache = new MapProfileCache();
    private final WorldManager worldManager = new OneBlockWorldManager();

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
}
