package es.noobcraft.oneblock.api;

import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.profile.ProfileCache;

public interface OneBlock {
    /**
     * Return the correct instance of PlayerCache
     * @return OneBlock PlayerCache
     */
    PlayerCache getPlayerCache();

    /**
     * Return the correct instance of ProfileCache
     * @return OneBlock ProfileCache
     */
    ProfileCache getProfileCache();
}
