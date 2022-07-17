package es.noobcraft.oneblock.world;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.world.WorldManager;
import es.noobcraft.oneblock.logger.Logger;
import es.noobcraft.oneblock.logger.LoggerType;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;

public class OneBlockWorldManager implements WorldManager {
    private final SlimePlugin slimePlugin = ((SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager"));

    @Override
    public boolean createWorld(OneBlockPlayer player, String name) throws WorldAlreadyExistsException {
        if (player.getProfiles().size() >= player.getMaxProfiles()) return false;

        try {
            slimePlugin.createEmptyWorld(slimePlugin.getLoader("one-block"), "", false, getProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean loadWorld(String name) throws CorruptedWorldException, UnknownWorldException {
        try {
            slimePlugin.loadWorld(slimePlugin.getLoader("one-block"), name, false, getProperties());
            return true;
        } catch (NewerFormatException | IOException | WorldInUseException  e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unloadWorld(String name) {
        final World world = Bukkit.getWorld(name);

        if (world != null || world.getPlayers() == null || !world.getPlayers().isEmpty()) {
            Logger.log(LoggerType.ERROR, "Error, world,couldn't be unloaded");
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
        properties.setInt(SlimeProperties.SPAWN_X, 123);
        properties.setInt(SlimeProperties.SPAWN_Y, 112);
        properties.setInt(SlimeProperties.SPAWN_Z, 170);
        return properties;
    }

}
