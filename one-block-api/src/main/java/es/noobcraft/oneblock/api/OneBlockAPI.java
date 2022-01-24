package es.noobcraft.oneblock.api;

import es.noobcraft.oneblock.api.flags.IslandPermissionManager;
import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.profile.ProfileCache;
import es.noobcraft.oneblock.api.profile.ProfileLoader;
import es.noobcraft.oneblock.api.profile.ProfileManager;
import es.noobcraft.oneblock.api.scoreboard.ScoreboardManager;
import es.noobcraft.oneblock.api.world.WorldManager;
import lombok.NonNull;

public class OneBlockAPI {
    private static OneBlock oneblock;

    public static void setOneBlock(final @NonNull OneBlock oneBlock) {
        if (OneBlockAPI.oneblock != null)
            throw new IllegalStateException("Cannot redefine singleton");
        OneBlockAPI.oneblock = oneBlock;
    }

    public static ProfileLoader getProfileLoader() {
        return oneblock.getProfileLoader();
    }

    public static IslandPermissionManager getIslandPermissionLoader() {
        return oneblock.getIslandPermissionManager();
    }

    public static PlayerCache getPlayerCache() {
        return oneblock.getPlayerCache();
    }

    public static ProfileCache getProfileCache() {
        return oneblock.getProfileCache();
    }

    public static WorldManager getWorldManager() {
        return oneblock.getWorldManager();
    }

    public static ProfileManager getProfileManager() {
        return oneblock.getProfileManager();
    }

    public static ScoreboardManager getScoreboardManager() {
        return oneblock.getScoreboardManager();
    }
}
