package es.noobcraft.oneblock.api;

import es.noobcraft.oneblock.api.profile.ProfileLoader;
import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.profile.ProfileManager;
import es.noobcraft.oneblock.api.world.WorldManager;

public interface OneBlock {
    /**
     * Return the correct instance of PlayerCache
     * @return OneBlock PlayerCache
     */
    PlayerCache getPlayerCache();

    /**
     * Return the correct instance of ProfileLoader
     * @return OneBlock ProfileLoader
     */
    ProfileLoader getProfileLoader();

    /**
     * Return the correct instance of WorldManager
     * @return OneBlock ProfileCache
     */
    WorldManager getWorldManager();

    /**
     * Return the correct instance of ProfileManager
     * @return OneBlock ProfileManager
     */
    ProfileManager getProfileManager();
}
