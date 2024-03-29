package es.noobcraft.oneblock;

import com.google.common.collect.Sets;
import com.grinderwolf.swm.api.SlimePlugin;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.commands.*;
import es.noobcraft.oneblock.listeners.*;
import es.noobcraft.oneblock.loaders.SQLSlimeLoader;
import es.noobcraft.oneblock.scoreboard.BaseScoreboardManager;
import es.noobcraft.oneblock.utils.Loaders;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class OneBlock extends OneBlockPlugin {
    @Override
    public void enable() {
        //Register one-block loader to SlimeWorld plugin
        ((SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager"))
                .registerLoader("one-block", new SQLSlimeLoader());

        //Start the scoreboard updater
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, BaseScoreboardManager::update,0L, 20L);

        //Sync all the worlds from redis
        OneBlockAPI.getServerCache().syncWorlds();

        //Load all the modules on /modules dir
        OneBlockAPI.getModuleLoader().getModules(new File(Core.getServerConfigurationsDirectory().getAbsolutePath()+ "/modules"))
                .forEach(OneBlockAPI.getModuleManager()::loadModule);

        Logger.log(LoggerType.CONSOLE, "OneBlock enabled successfully");
    }

    @Override
    @SuppressWarnings("all")
    public void disable() {
        //Force saving player if plugin is disabled
        for (OneBlockPlayer oneBlockPlayer : OneBlockAPI.getPlayerCache().getPlayers()) {
            if (oneBlockPlayer.getCurrentProfile() == null) return;

            Logger.log(LoggerType.CONSOLE, "Forcing to unload player "+ oneBlockPlayer.getName());
            Loaders.unloadPlayer(oneBlockPlayer, oneBlockPlayer.getCurrentProfile());
        }
        BaseScoreboardManager.clearScoreBoards();
        //Remove all temp_ files from the dir
        Arrays.stream(Bukkit.getWorldContainer().listFiles((dir, name) -> name.startsWith("temp_")))
                .forEach(this::deleteFile);
    }

    @Override
    public Set<PlayerCommand> loadCommand() {
        return Sets.newHashSet(Arrays.asList(new ProfileCommand(), new PermissionCommand(), new StatusCommand(),
                new GotoCommand(), new CoopAcceptCommand(), new CoopInviteCommand(), new CoopRemoveCommand(),
                new LobbyCommand()));
    }

    @Override
    public Set<Listener> registerListeners() {
        return Sets.newHashSet(Arrays.asList(new PlayerListeners(), new IslandListeners(), new ItemListeners(), new PhaseListeners(this),
                new InfiniteBlockListener(this), new PhaseUpgradeListeners(this), new LobbyListeners()));
    }

    @SuppressWarnings("all")
    public void deleteFile(@NonNull File path) {
        if (path.listFiles() != null)
        for (File file : Objects.requireNonNull(path.listFiles()))
            if (file.isDirectory()) deleteFile(file);
            else file.delete();
        path.delete();
    }
}
