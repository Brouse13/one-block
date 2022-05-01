package es.noobcraft.oneblock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.noobcraft.oneblock.adapters.*;
import es.noobcraft.oneblock.api.OneBlock;
import es.noobcraft.oneblock.api.loaders.PhaseLoader;
import es.noobcraft.oneblock.api.loaders.ProfileLoader;
import es.noobcraft.oneblock.api.loaders.SettingsLoader;
import es.noobcraft.oneblock.api.loaders.WorldLoader;
import es.noobcraft.oneblock.api.permission.PermissionManager;
import es.noobcraft.oneblock.api.player.PlayerCache;
import es.noobcraft.oneblock.api.player.PlayerSupplier;
import es.noobcraft.oneblock.api.profile.ProfileCache;
import es.noobcraft.oneblock.api.scoreboard.ScoreboardManager;
import es.noobcraft.oneblock.api.settings.OneBlockConstants;
import es.noobcraft.oneblock.api.settings.OneBlockSettings;
import es.noobcraft.oneblock.loaders.JSONPhaseLoader;
import es.noobcraft.oneblock.loaders.SQLProfileLoader;
import es.noobcraft.oneblock.loaders.SlimeWorldLoader;
import es.noobcraft.oneblock.permission.SetPermissionManager;
import es.noobcraft.oneblock.phase.*;
import es.noobcraft.oneblock.player.BasePlayerSupplier;
import es.noobcraft.oneblock.player.SetPlayerCache;
import es.noobcraft.oneblock.profile.SetProfileCache;
import es.noobcraft.oneblock.scoreboard.BaseScoreboardManager;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;

public class API implements OneBlock {
    private final PhaseLoader phaseLoader = new JSONPhaseLoader();
    private final PlayerCache playerCache = new SetPlayerCache();
    private final ProfileCache profileCache = new SetProfileCache();
    private final ProfileLoader profileLoader = new SQLProfileLoader();
    private final PermissionManager permissionsManager = new SetPermissionManager();
    private final WorldLoader worldLoader = new SlimeWorldLoader();
    private final ScoreboardManager scoreboardManager = new BaseScoreboardManager();
    private final PlayerSupplier playerSupplier = new BasePlayerSupplier();
    private final OneBlockSettings settings = new OneBlockConstants();

    @Override
    public PhaseLoader getPhaseLoader() {
        return phaseLoader;
    }

    @Override
    public ProfileLoader getProfileLoader() {
        return profileLoader;
    }

    @Override
    public PermissionManager getPermissionManager() {
        return permissionsManager;
    }

    @Override
    public PlayerCache getPlayerCache() {
        return playerCache;
    }

    @Override
    public PlayerSupplier getPlayerSupplier() {
        return playerSupplier;
    }

    @Override
    public ProfileCache getProfileCache() {
        return profileCache;
    }

    @Override
    public WorldLoader getWorldLoader() {
        return worldLoader;
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    @Override
    public SettingsLoader getSettingsLoader() {
        throw new NotImplementedException("This functionality is not implemented yet");
    }

    @Override
    public OneBlockSettings getSettings() {
        return settings;
    }

    @Override
    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(BasePhase.class, new PhaseAdapter())
                .registerTypeAdapter(BaseLootTable.class, new LootTableAdapter())
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeAdapter(BaseSpecialActions.class, new SpecialActionsAdapter())
                .registerTypeAdapter(BaseMobType.class, new MobTypeAdapter())
                .registerTypeAdapter(BaseBlockType.class, new BlockTypeAdapter())
                .serializeNulls().create();
    }
}
