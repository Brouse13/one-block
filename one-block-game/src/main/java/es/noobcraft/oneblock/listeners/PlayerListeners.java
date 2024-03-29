package es.noobcraft.oneblock.listeners;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.event.AsyncNoobPlayerPreLoginEvent;
import es.noobcraft.core.api.event.NoobPlayerJoinEvent;
import es.noobcraft.core.api.event.NoobPlayerQuitEvent;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.scoreboard.LobbyScoreBoard;
import es.noobcraft.oneblock.utils.Items;
import es.noobcraft.oneblock.utils.Loaders;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.Vector;

import java.util.Optional;

public class PlayerListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onAsyncNoobPlayerPreLogin(AsyncNoobPlayerPreLoginEvent event) {
        //Check if the event was cancelled by another event
        if (!event.getResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) return;

        //Add the player to the cache
        OneBlockPlayer player = OneBlockAPI.getPlayerSupplier().createPlayer(event.getPlayer().getUsername());
        OneBlockAPI.getPlayerCache().addPlayer(player);

        //Load all the player profiles to the player and the cache
        OneBlockAPI.getProfileLoader().loadProfiles(player).ifPresent(profiles -> profiles.forEach(profile -> {
            player.addProfile(profile);
            OneBlockAPI.getProfileCache().addProfile(profile);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onNoobPlayerJoin(NoobPlayerJoinEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getNoobPlayer().getUsername());

        //Check if the player has any teleportRequest
        Optional<String> world = OneBlockAPI.getServerLoader().hasTeleportRequest(event.getNoobPlayer().getName(), Core.getServerId());
        if (world.isPresent()) {
            //Check if the player has a profile on the retrieved world or else ignore teleportRequest
            OneBlockProfile worldProfile = player.getProfiles().stream()
                    .filter(profile -> profile.getWorldName().equals(world.get()))
                    .findFirst().orElse(null);

            //Found profile on retrieved world and cancel the rest of the join methods
            if (worldProfile != null) {
                OneBlockAPI.getServerLoader().removeTeleportRequest(player.getName());
                Loaders.loadProfile(worldProfile, player);
                return;
            }
        }

        //Create the player scoreboard and teleport it to spawn
        player.setScoreBoard(new LobbyScoreBoard(player));
        event.getNoobPlayer().teleport(OneBlockAPI.getSettings().getLobbySpawn());

        //Add the profiles item
        event.getNoobPlayer().getInventory().setItem(0,
                SpigotCore.getImmutableItemManager().makeImmutable(Items.getLobbyItem(player)));
    }

    @EventHandler(ignoreCancelled = true)
    public void onNoobPlayerQuit(NoobPlayerQuitEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getNoobPlayer().getName());

        //Teleport player to spawn
        player.getBukkitPlayer().teleport(OneBlockAPI.getSettings().getLobbySpawn());

        //If player is on a profile encode its inventory
        if (player.getCurrentProfile() != null)
            Loaders.unloadPlayer(player, player.getCurrentProfile());

        //Update profiles and remove them from the cache
        player.getProfiles().forEach(profile -> {
            OneBlockAPI.getProfileLoader().updateProfile(profile);
            OneBlockAPI.getProfileCache().removeProfile(profile);
            if (OneBlockAPI.getWorldLoader().unloadWorld(profile.getWorldName()))
                OneBlockAPI.getPermissionManager().removeCache(profile.getWorldName());
        });
        player.setScoreBoard(null);
        OneBlockAPI.getPlayerCache().removePlayer(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());

        //If player is on a profile check the if the infinity block exists to add it
        if (player.getCurrentProfile() != null) {
            //Create the world and location of the block at the player island
            World world = Bukkit.getWorld(player.getCurrentProfile().getWorldName());
            Location location = OneBlockAPI.getSettings().getIslandSpawn().toLocation(world);

            if (world.getBlockAt(location).getType() == Material.AIR)
                world.getBlockAt(location).setType(Material.GRASS);

            event.setRespawnLocation(location.clone().add(new Vector(0, 1, 0)));
        }else {
            //Give the player the menu item if is not on a profile
            event.getPlayer().getInventory().setItem(0, SpigotCore.getImmutableItemManager().makeImmutable(Items.getLobbyItem(player)));
        }
        event.getPlayer().teleport(OneBlockAPI.getSettings().getLobbySpawn());
    }
}
