package es.noobcraft.oneblock.world;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import es.noobcraft.oneblock.api.world.WorldManager;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;

public class OneBlockWorldManager implements WorldManager {
    private final SlimePlugin slimePlugin = ((SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager"));

    @Override
    public boolean createWorld(String name, boolean load) {
        try {
            SlimeWorld emptyWorld = slimePlugin.createEmptyWorld(slimePlugin.getLoader("one-block"), name, false, getProperties());
            slimePlugin.generateWorld(emptyWorld);

            if (load) loadWorld(name, false);
            return true;
        } catch (IOException | WorldAlreadyExistsException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean loadWorld(String name, boolean locked) {
        try {
            if (Bukkit.getWorld(name) != null) return true;

            final SlimeWorld slimeWorld = slimePlugin.loadWorld(slimePlugin.getLoader("one-block"), name, locked, getProperties());
            slimePlugin.generateWorld(slimeWorld);
            return true;

        } catch (NewerFormatException | IOException | WorldInUseException | CorruptedWorldException | UnknownWorldException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean unloadWorld(String name) {
        final World world = Bukkit.getWorld(name);

        if (world == null) {
            Logger.log(LoggerType.ERROR, "World is already unloaded");
            return false;
        }

        if (!Bukkit.unloadWorld(world, true)) {
            Logger.log(LoggerType.ERROR, "Unable to save world " + name);
            return false;
        }
        return true;
    }

    private SlimePropertyMap getProperties() {
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setString(SlimeProperties.DIFFICULTY, "hard");
        properties.setInt(SlimeProperties.SPAWN_X, 0);
        properties.setInt(SlimeProperties.SPAWN_Y, 30);
        properties.setInt(SlimeProperties.SPAWN_Z, 0);
        return properties;
    }

}
