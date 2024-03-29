package es.noobcraft.oneblock.api;

import com.google.gson.Gson;
import es.noobcraft.oneblock.api.loaders.PhaseLoader;
import es.noobcraft.oneblock.api.loaders.ProfileLoader;
import es.noobcraft.oneblock.api.loaders.SettingsLoader;
import es.noobcraft.oneblock.api.loaders.WorldLoader;
import es.noobcraft.oneblock.api.module.ModuleLoader;
import es.noobcraft.oneblock.api.module.ModuleManager;
import es.noobcraft.oneblock.api.permission.PermissionManager;
import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.player.PlayerSupplier;
import es.noobcraft.oneblock.api.profile.ProfileCache;
import es.noobcraft.oneblock.api.server.ServerCache;
import es.noobcraft.oneblock.api.server.ServerLoader;
import es.noobcraft.oneblock.api.settings.OneBlockSettings;

public interface OneBlock {
    /**
     * Get the PhaseLoader instance that will load
     * phases and
     * @return OneBlock PhaseLoader
     */
    PhaseLoader getPhaseLoader();

    /**
     * Get the ProfileLoader instance that will load all
     * the player profiles from the correct database.
     * @return OneBlock ProfileLoader
     */
    ProfileLoader getProfileLoader();

    /**
     * Get the PermissionManager instance that will
     * manage the perms cache and database update
     * @return OneBlock IslandPermissionsLoader
     */
    PermissionManager getPermissionManager();

    /**
     * Get the PlayerCache instance that will storage
     * the cache of all the online OneBlockPlayers.
     * @return OneBlock PlayerCache
     */
    PlayerCache getPlayerCache();

    /**
     * Get the PlayerSupplier instance that manages the creation
     * of OneBlockPlayers
     * @return the OneBlock PlayerSupplier
     */
    PlayerSupplier getPlayerSupplier();

    /**
     * Get the ProfileCache instance that store all the
     * available profiles from players
     * @return OneBlock ProfileCache
     */
    ProfileCache getProfileCache();

    /**
     * Get the WorldLoader instance that will manage
     * the creation of worlds.
     * @return OneBlock WorldManager
     */
    WorldLoader getWorldLoader();

    /**
     * Get the SettingsLoader instance that will
     * load the default settings for OneBlock
     * @return OneBlock SettingsLoader
     */
    SettingsLoader getSettingsLoader();

    /**
     * Get the default settings for OneBlock
     * @return OneBlock Settings
     */
    OneBlockSettings getSettings();

    /**
     * Get the cache containing all the loaded
     * worlds withs it's respective worlds
     * @return OneBlock ServerCache
     */
    ServerCache getServerCache();

    /**
     * Get the loader of the severs from the
     * Database
     * @return OneBlock ServerLoader
     */
    ServerLoader getServerLoader();

    /**
     * Get the loader of the Modules on /modules
     * dir
     * @return OneBlock moduleLoader
     */
    ModuleLoader getModuleLoader();

    /**
     * Get the manager of the modules. It will load
     * unload... the modules from the plugin
     * @return OneBlock moduleManager
     */
    ModuleManager getModuleManager();

    /**
     * Get the default gson with all the TypeAdapters
     * for the oneBlock
     * @return the OneBlock Gson instance
     */
    Gson getGson();
}
