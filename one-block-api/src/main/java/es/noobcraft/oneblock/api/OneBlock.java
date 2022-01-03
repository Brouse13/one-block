package es.noobcraft.oneblock.api;

import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.profile.ProfileCache;
import es.noobcraft.oneblock.api.profile.ProfileLoader;
import es.noobcraft.oneblock.api.profile.ProfileManager;
import es.noobcraft.oneblock.api.world.WorldManager;

public interface OneBlock {
    /**
     * Get the PlayerCache instance that will storage
     * the cache of all the online OneBlockPlayers.
     * @return OneBlock PlayerCache
     */
    PlayerCache getPlayerCache();

    /**
     * Get the ProfileLoader instance that will load all
     * the player profiles from the correct database.
     * @return OneBlock ProfileLoader
     */
    ProfileLoader getProfileLoader();

    /**
     * Get the ProfileCache instance that store all the
     * available profiles from players
     * @return OneBlock ProfileCache
     */
    ProfileCache getProfileCache();

    /**
     * Get the WorldManager instance that will manage
     * the creation of SlimeWorlds.
     * @return OneBlock WorldManager
     */
    WorldManager getWorldManager();

    /**
     * Get the ProfileManager that will manage the creation
     * of coops and other profile utils
     * @return OneBlock ProfileManager
     */
    ProfileManager getProfileManager();
}
