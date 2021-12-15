package es.noobcraft.oneblock;

import es.noobcraft.oneblock.api.OneBlock;
import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.profile.ProfileCache;
import es.noobcraft.oneblock.player.SetOneBlockPlayerCache;
import es.noobcraft.oneblock.profile.MapProfileCache;

public class API implements OneBlock {
    private final PlayerCache playerCache = new SetOneBlockPlayerCache();
    private final ProfileCache profileCache = new MapProfileCache();

    @Override
    public PlayerCache getPlayerCache() {
        return playerCache;
    }

    @Override
    public ProfileCache getProfileCache() {
        return profileCache;
    }
}
