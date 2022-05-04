package es.noobcraft.oneblock;

import com.google.common.collect.Sets;
import com.grinderwolf.swm.api.SlimePlugin;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.commands.PermissionCommand;
import es.noobcraft.oneblock.commands.ProfileCommand;
import es.noobcraft.oneblock.listeners.*;
import es.noobcraft.oneblock.utils.Loaders;
import es.noobcraft.oneblock.world.SQLOneBlockLoader;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.Arrays;
import java.util.Set;

public class OneBlock extends OneBlockPlugin {
    @Override
    public void enable() {
        //Register one-block loader to SlimeWorld plugin
        ((SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager"))
                .registerLoader("one-block", new SQLOneBlockLoader());
        //Start the scoreboard updater
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this,
                () -> OneBlockAPI.getScoreboardManager().update(),0L, 20L);
        //Sync all the worlds from redis
        OneBlockAPI.getServerCache().syncWorlds();
        Logger.log(LoggerType.CONSOLE, "OneBlock enabled successfully");
    }

    @Override
    public void disable() {
        //Force saving player if plugin is disabled
        for (OneBlockPlayer oneBlockPlayer : OneBlockAPI.getPlayerCache().getPlayers()) {
            if (oneBlockPlayer.getCurrentProfile() == null) return;

            Logger.log(LoggerType.CONSOLE, "Forcing to unload player "+ oneBlockPlayer.getName());
            Loaders.unloadPlayer(oneBlockPlayer, oneBlockPlayer.getCurrentProfile());
        }
        OneBlockAPI.getScoreboardManager().clearScoreBoards();
        //Remove all temp_ files from the dir
        Arrays.stream(Bukkit.getWorldContainer().listFiles((dir, name) -> name.startsWith("temp_"))).forEach(this::deleteFile);
    }

    @Override
    public Set<PlayerCommand> loadCommand() {
        return Sets.newHashSet(Arrays.asList(new ProfileCommand(), new PermissionCommand()));
    }

    @Override
    public Set<Listener> registerListeners() {
        return Sets.newHashSet(Arrays.asList(new PlayerListeners(), new IslandListeners(), new ItemListeners(), new PhaseListeners(this),
                new InfiniteBlockListener(), new PhaseUpgradeListeners(this)));
    }

    public void deleteFile(File path) {
        for (File file : path.listFiles())
            if (file.isDirectory()) deleteFile(file);
            else file.delete();
        path.delete();
    }
}
