package es.noobcraft.oneblock;

import com.google.common.collect.Sets;
import com.grinderwolf.swm.api.SlimePlugin;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.commands.ProfileCommand;
import es.noobcraft.oneblock.listeners.PlayerListeners;
import es.noobcraft.oneblock.loaders.PlayerLoader;
import es.noobcraft.oneblock.logger.Logger;
import es.noobcraft.oneblock.logger.LoggerType;
import es.noobcraft.oneblock.world.OneBlockLoader;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Set;

public class OneBlock extends OneBlockPlugin {
    @SneakyThrows
    @Override
    public void enable() {
        SlimePlugin slimePlugin = ((SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager"));
        slimePlugin.registerLoader("one-block", new OneBlockLoader());
        registerServer();
        Logger.log(LoggerType.CONSOLE, "OneBlock enabled successfully");
    }

    @Override
    public void disable() {
        for (OneBlockPlayer oneBlockPlayer : OneBlockAPI.getPlayerCache().getPlayers()) {
            if (oneBlockPlayer.getCurrentProfile() == null) return;

            Logger.log(LoggerType.CONSOLE, "Forcing to unload player "+ oneBlockPlayer.getName());
            PlayerLoader.unloadPlayer(oneBlockPlayer, oneBlockPlayer.getCurrentProfile());
        }
    }

    @Override
    public Set<PlayerCommand> loadCommand() {
        return Sets.newHashSet(Arrays.asList(new ProfileCommand()));
    }

    @Override
    public Set<Listener> registerListeners() {
        return Sets.newHashSet(Arrays.asList(new PlayerListeners()));
    }

    private void registerServer() {

    }
}
