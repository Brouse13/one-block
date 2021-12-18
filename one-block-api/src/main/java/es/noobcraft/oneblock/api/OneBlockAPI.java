package es.noobcraft.oneblock.api;

import es.noobcraft.oneblock.api.database.ProfileLoader;
import es.noobcraft.oneblock.api.player.PlayerCache;
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

    public static PlayerCache getPlayerCache() {
        return oneblock.getPlayerCache();
    }

    public static WorldManager getWorldManager() {
        return oneblock.getWorldManager();
    }
}
