package es.noobcraft.oneblock.api.module;

import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.command.ConsoleCommand;
import es.noobcraft.core.api.command.PlayerCommand;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class OneBlockModule {
    @Getter private String name;
    @Getter private OneBlockModuleSettings settings;

    private JavaPlugin plugin;

    public void load() {
    }

    public void unload() {
    }

    public void registerListener(Listener listener) {
        if (plugin == null) throw new IllegalStateException("Module hasn't initialized");
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public void registerCommand(PlayerCommand command) {
        SpigotCore.getCommandManager().add(command);
    }

    public void registerCommand(ConsoleCommand command) {
        SpigotCore.getCommandManager().add(command);
    }

    public void init(JavaPlugin plugin, String name, OneBlockModuleSettings settings) {
        this.plugin = plugin;
        this.name = name;
        this.settings = settings;
    }
}
