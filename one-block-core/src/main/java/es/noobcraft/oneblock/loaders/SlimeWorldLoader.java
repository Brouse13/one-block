package es.noobcraft.oneblock.loaders;

import com.google.common.collect.Sets;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.loaders.WorldLoader;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class SlimeWorldLoader implements WorldLoader {
    private final SlimePlugin slimePlugin = ((SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager"));

    @Override
    public boolean createWorld(String worldName) {
        try {
            SlimeWorld emptyWorld = slimePlugin.createEmptyWorld(slimePlugin.getLoader("one-block"), worldName, false, getProperties());
            slimePlugin.generateWorld(emptyWorld);
            loadWorld(worldName, false);
            return true;
        } catch (IOException | WorldAlreadyExistsException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean loadWorld(String worldName, boolean locked) {
        if (Bukkit.getWorld(worldName) != null) return true;
        try {
            SlimeWorld slimeWorld = slimePlugin.loadWorld(slimePlugin.getLoader("one-block"), worldName, false, getProperties());
            slimePlugin.generateWorld(slimeWorld);
            return true;

        } catch (IOException | UnknownWorldException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean unloadWorld(String worldName) {
        final World world = Bukkit.getWorld(worldName);

        if (world == null) {
            Logger.log(LoggerType.ERROR, "World is already unloaded");
            return false;
        }

        //Teleport all te players to the lobby if the
        List<Player> onlinePlayers = Bukkit.getWorld(worldName).getPlayers();
        Set<OneBlockPlayer> players = Sets.newHashSet();

        onlinePlayers.stream().map(player -> OneBlockAPI.getPlayerCache().getPlayer(player.getName())).forEach(players::add);

        if (players.size() == onlinePlayers.size()) {
            players.forEach(player -> {
                player.getBukkitPlayer().teleport(OneBlockAPI.getSettings().getLobbySpawn());
                Logger.player(player.getNoobPlayer(), "one-block.messages.world-unloading");
            });
        }

        if (!Bukkit.unloadWorld(world, true)) {
            Logger.log(LoggerType.ERROR, "Unable to save world " + worldName);
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
