package es.noobcraft.oneblock;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.grinderwolf.swm.api.SlimePlugin;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.register.ConnectMode;
import es.noobcraft.core.api.register.ConstantSupplier;
import es.noobcraft.core.api.register.PropertyConstants;
import es.noobcraft.oneblock.commands.ProfileCommand;
import es.noobcraft.oneblock.listeners.PlayerListeners;
import es.noobcraft.oneblock.logger.Logger;
import es.noobcraft.oneblock.logger.LoggerType;
import es.noobcraft.oneblock.world.OneBlockLoader;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
