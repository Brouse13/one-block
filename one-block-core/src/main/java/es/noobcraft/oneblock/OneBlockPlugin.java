package es.noobcraft.oneblock;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.register.ConnectMode;
import es.noobcraft.core.api.register.ConstantSupplier;
import es.noobcraft.core.api.register.PropertyConstants;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.loaders.JSONPhaseLoader;
import es.noobcraft.oneblock.server.RedisServerSubscriber;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public abstract class OneBlockPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        OneBlockAPI.setOneBlock(new API());
        OneBlockAPI.getPhaseLoader().loadPhases();
        Logger.log(LoggerType.CONSOLE, "Api loaded successfully");
        JSONPhaseLoader.scheduleUpdate(this);
        enable();
        registerListeners().forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
        loadCommand().forEach(command -> SpigotCore.getCommandManager().add(command));
        registerServer();
    }

    @Override
    public void onDisable() {
        disable();
    }

    @Override
    public void onLoad() {
        new Logger(this);
        Core.getRedisClient().subscribe("one-block", new RedisServerSubscriber());
    }

    public abstract void enable();

    public abstract void disable();

    public abstract Set<PlayerCommand> loadCommand();

    public abstract Set<Listener> registerListeners();

    private void registerServer() {
        /*
        Core.getServerRegistryManager().registryProperty(
                PropertyConstants.GAME_MODE_PROPERTY_SPECIAL, new ConstantSupplier<>(Boolean.FALSE.toString()));
        Core.getServerRegistryManager().registryProperty(
                PropertyConstants.GAME_MODE_PROPERTY_CONNECT_MODE, new ConstantSupplier<>(ConnectMode.SELECTION.toString()));
        Core.getServerRegistryManager().registryProperty(
                PropertyConstants.GAME_MODE_ITEM_PROPERTY_MATERIAL, new ConstantSupplier<>(Material.BEACON.toString()));
        Core.getServerRegistryManager().registryProperty(
                PropertyConstants.GAME_MODE_ITEM_PROPERTY_AMOUNT, new ConstantSupplier<>("1"));
        Core.getServerRegistryManager().registryProperty(
                PropertyConstants.GAME_MODE_ITEM_PROPERTY_DAMAGE, new ConstantSupplier<>("0"));
        Core.getServerRegistryManager().registryProperty(
                PropertyConstants.GAME_MODE_SERVER_PROPERTY_SUPPORTED_VERSIONS, new ConstantSupplier<>("1.8.8 - 1.16.5"));
        Core.getServerRegistryManager().registryProperty(
                PropertyConstants.GAME_MODE_SERVER_PROPERTY_PLAYERS_ONLINE, () -> getServer().getOnlinePlayers().size());
        Core.getServerRegistryManager().registryProperty(
                PropertyConstants.GAME_MODE_SERVER_PROPERTY_PLAYERS_LIMIT, () -> Bukkit.getServer().getMaxPlayers());
         */
        Core.getServerRegistryManager().registryProperty(
                PropertyConstants.SERVER_PROPERTY_RESTRICTED, new ConstantSupplier<>(Boolean.FALSE.toString()));
        Core.getServerRegistryManager().registryProperty(
                PropertyConstants.SERVER_PROPERTY_ALLOWED_GROUPS, new ConstantSupplier<>(Integer.toString(-1)));
    }
}
