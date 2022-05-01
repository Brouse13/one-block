package es.noobcraft.oneblock.listeners;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.event.AsyncNoobPlayerPreLoginEvent;
import es.noobcraft.core.api.event.NoobPlayerJoinEvent;
import es.noobcraft.core.api.event.NoobPlayerQuitEvent;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.inventory.InventorySerializer;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.scoreboard.LobbyScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerListeners implements Listener {
    private final Translator translator = Core.getTranslator();

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
        //Create the player scoreboard and teleport it to spawn
        OneBlockAPI.getScoreboardManager().createScoreboard(player, new LobbyScoreBoard(player));
        event.getNoobPlayer().teleport(OneBlockAPI.getSettings().getLobbySpawn());

        //Add the profiles item
        event.getNoobPlayer().getInventory().setItem(0, SpigotCore.getImmutableItemManager().makeImmutable(
                ItemBuilder.from(Material.NETHER_STAR)
                        .displayName(translator.getLegacyText(event.getNoobPlayer(), "one-block.inventory.player.profile-list.name"))
                        .lore(translator.getLegacyTextList(event.getNoobPlayer(), "one-block.inventory.player.profile-list.lore"))
                        .metadata("event", "profile-list").build()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onNoobPlayerQuit(NoobPlayerQuitEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getNoobPlayer().getName());

        //Teleport player to spawn
        player.getBukkitPlayer().teleport(OneBlockAPI.getSettings().getLobbySpawn());

        //If player is on a profile encode its inventory
        if (player.getCurrentProfile() != null) {
            List<ItemStack> inventory = new ArrayList<>();
            inventory.addAll(Arrays.asList(player.getBukkitPlayer().getInventory().getArmorContents()));
            inventory.addAll(Arrays.asList(player.getBukkitPlayer().getInventory().getContents()));

            player.getCurrentProfile().setInventory(InventorySerializer.serialize(inventory.toArray(new ItemStack[0])));
            player.getBukkitPlayer().getInventory().clear();
        }

        //Update profiles and remove them from the cache
        player.getProfiles().forEach(profile -> {
            OneBlockAPI.getProfileLoader().updateProfile(profile);
            OneBlockAPI.getProfileCache().removeProfile(profile);
            OneBlockAPI.getWorldLoader().unloadWorld(profile.getWorldName());
        });
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

            event.setRespawnLocation(location.add(new Vector(0, 1, 0)));
        }
        event.getPlayer().teleport(OneBlockAPI.getSettings().getLobbySpawn());
    }
}
