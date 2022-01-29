package es.noobcraft.oneblock.api;

import com.google.gson.Gson;
import es.noobcraft.oneblock.api.flags.IslandPermissionManager;
import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.profile.ProfileCache;
import es.noobcraft.oneblock.api.profile.ProfileLoader;
import es.noobcraft.oneblock.api.profile.ProfileManager;
import es.noobcraft.oneblock.api.scoreboard.ScoreboardManager;
import es.noobcraft.oneblock.api.world.WorldManager;

public interface OneBlock {
    /**
     * Get the ProfileLoader instance that will load all
     * the player profiles from the correct database.
     * @return OneBlock ProfileLoader
     */
    ProfileLoader getProfileLoader();

    /**
     * Get the IslandPermissionManager instance that will
     * manage the perms cache and database update
     * @return OneBlock IslandPermissionsLoader
     */
    IslandPermissionManager getIslandPermissionManager();

    /**
     * Get the PlayerCache instance that will storage
     * the cache of all the online OneBlockPlayers.
     * @return OneBlock PlayerCache
     */
    PlayerCache getPlayerCache();

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

    /**
     * Get the ScoreboardManager that will manage the creation
     * and deletion of player scoreboards
     * @return OneBlock ScoreboardManager
     */
    ScoreboardManager getScoreboardManager();

    /**
     * Get the default gson with all the TypeAdapters
     * for the oneBlock
     * @return the OneBlock Gson instance
     */
    Gson getGson();
}
