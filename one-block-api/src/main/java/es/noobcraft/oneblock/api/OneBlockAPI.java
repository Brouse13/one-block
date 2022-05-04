package es.noobcraft.oneblock.api;

import com.google.gson.Gson;
import es.noobcraft.oneblock.api.loaders.PhaseLoader;
import es.noobcraft.oneblock.api.loaders.ProfileLoader;
import es.noobcraft.oneblock.api.loaders.SettingsLoader;
import es.noobcraft.oneblock.api.loaders.WorldLoader;
import es.noobcraft.oneblock.api.permission.PermissionManager;
import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.player.PlayerSupplier;
import es.noobcraft.oneblock.api.profile.ProfileCache;
import es.noobcraft.oneblock.api.scoreboard.ScoreboardManager;
import es.noobcraft.oneblock.api.server.ServerCache;
import es.noobcraft.oneblock.api.server.ServerLoader;
import es.noobcraft.oneblock.api.settings.OneBlockSettings;
import lombok.NonNull;

public class OneBlockAPI {
    private static OneBlock oneblock;

    public static void setOneBlock(final @NonNull OneBlock oneBlock) {
        if (OneBlockAPI.oneblock != null)
            throw new IllegalStateException("Cannot redefine singleton");
        OneBlockAPI.oneblock = oneBlock;
    }

    public static PhaseLoader getPhaseLoader() {
        return oneblock.getPhaseLoader();
    }

    public static ProfileLoader getProfileLoader() {
        return oneblock.getProfileLoader();
    }

    public static PermissionManager getPermissionManager() {
        return oneblock.getPermissionManager();
    }

    public static PlayerCache getPlayerCache() {
        return oneblock.getPlayerCache();
    }

    public static PlayerSupplier getPlayerSupplier() {
        return oneblock.getPlayerSupplier();
    }

    public static ProfileCache getProfileCache() {
        return oneblock.getProfileCache();
    }

    public static WorldLoader getWorldLoader() {
        return oneblock.getWorldLoader();
    }

    public static ScoreboardManager getScoreboardManager() {
        return oneblock.getScoreboardManager();
    }

    public static SettingsLoader getSettingsLoader() {
        return oneblock.getSettingsLoader();
    }

    public static OneBlockSettings getSettings() {
        return oneblock.getSettings();
    }

    public static ServerLoader getServerLoader() {
        return oneblock.getServerLoader();
    }

    public static ServerCache getServerCache() {
        return oneblock.getServerCache();
    }

    public static Gson getGson() {
        return oneblock.getGson();
    }
}
