package es.noobcraft.oneblock.api;

import es.noobcraft.oneblock.api.player.PlayerCache;
import lombok.NonNull;

public class OneBlockAPI {
    private static OneBlock oneblock;

    public static void setOneBlock(final @NonNull OneBlock oneBlock) {
        if (OneBlockAPI.oneblock != null)
            throw new IllegalStateException("Cannot redefine singleton");
        OneBlockAPI.oneblock = oneBlock;
    }

    public static PlayerCache getPlayerCache() {
        return oneblock.getPlayerCache();
    }

}
