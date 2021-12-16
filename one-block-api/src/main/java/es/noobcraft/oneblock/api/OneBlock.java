package es.noobcraft.oneblock.api;

import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.profile.ProfileCache;
import es.noobcraft.oneblock.api.world.WorldManager;

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

    /**
     * Return the correct instance of WorldManager
     * @return OneBlock ProfileCache
     */
    WorldManager getWorldManager();
}
