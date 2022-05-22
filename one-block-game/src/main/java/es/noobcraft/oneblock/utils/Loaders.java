package es.noobcraft.oneblock.utils;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.inventory.InventorySerializer;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.profile.ProfileName;
import es.noobcraft.oneblock.profile.BaseOneBlockProfile;
import es.noobcraft.oneblock.scoreboard.IslandScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class Loaders {
    private static final Translator translator = Core.getTranslator();

    public static void loadProfile(OneBlockProfile profile, OneBlockPlayer player) {
        //Check if the ServerCache has load the world on another server
        Optional<String> server = OneBlockAPI.getServerCache().getServer(profile.getWorldName());
        if (server.isPresent() && !server.get().equals(Core.getServerId())) {
            Core.getServerConnectManager().connect(player.getNoobPlayer(), server.get());
            OneBlockAPI.getServerLoader().teleportRequest(profile, server.get());
            return;
        }

        //Load the world and world
        if (!OneBlockAPI.getWorldLoader().loadWorld(profile.getWorldName(), false)) {
            Logger.player(player.getNoobPlayer(), "one-block.messages.world-in-use");
            return;
        }

        player.setCurrentProfile(profile);
        OneBlockAPI.getPhaseLoader().getPhaseBlocks(profile.getWorldName());
        player.setScoreBoard(new IslandScoreBoard(profile));

        //Load the inventory to the player
        ItemStack[] deserialize;
        try {
            deserialize = InventorySerializer.deserialize(profile.getInventory());
        } catch (IOException e) {
            e.printStackTrace();
            Logger.player(player.getNoobPlayer(), "one-block.island.error.inventory-deserialize");
            return;
        }

        //Load player inv and teleport to default block
        Player bukkitPlayer = Bukkit.getPlayer(player.getName());
        PlayerInventory inventory = bukkitPlayer.getInventory();
        OneBlockAPI.getServerLoader().addWorld(Core.getServerId(), profile.getWorldName());

        ItemStack item = inventory.getItem(0);
        if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta() && item.getItemMeta().hasDisplayName())
            inventory.setItem(0, SpigotCore.getImmutableItemManager().makeMutable(item));
        inventory.clear();

        if (deserialize.length != 0) {
            inventory.setArmorContents(Arrays.copyOfRange(deserialize, 0, 4));
            inventory.setContents(Arrays.copyOfRange(deserialize, 4, deserialize.length));
        }

        Logger.player(player.getNoobPlayer(), "one-block.island.teleport-island");

        player.teleport(profile);
    }

    public static void unloadPlayer(OneBlockPlayer oneBlockPlayer, OneBlockProfile profile) {
        Player bukkitPlayer = Bukkit.getPlayer(oneBlockPlayer.getName());

        //Encode current player inventory and add it to the profile
        List<ItemStack> inventory = new ArrayList<>();
        inventory.addAll(Arrays.asList(bukkitPlayer.getInventory().getArmorContents()));
        inventory.addAll(Arrays.asList(bukkitPlayer.getInventory().getContents()));
        profile.setInventory(InventorySerializer.serialize(inventory.toArray(new ItemStack[0])));

        //Set to null the current profile to null
        oneBlockPlayer.setCurrentProfile(null);

        //More action to perform on to unload a profile
        OneBlockAPI.getServerLoader().removeWorld(Core.getServerId(), profile.getWorldName());
        OneBlockAPI.getProfileLoader().updateProfile(profile);
        bukkitPlayer.getInventory().clear();

        //Give the menu item and teleport to the lobby if player hasn't disconnect
        if (Core.getPlayerCache().getPlayer(oneBlockPlayer.getName()) != null) {
            bukkitPlayer.getInventory().setItem(0,
                    SpigotCore.getImmutableItemManager().makeImmutable(Items.getLobbyItem(oneBlockPlayer)));
            bukkitPlayer.teleport(OneBlockAPI.getSettings().getLobbySpawn());
        }
    }

    public static void createProfile(OneBlockPlayer player) {
        //Create a new empty profile and add it to database
        OneBlockProfile profile = new BaseOneBlockProfile(player, player.getName(),
                String.valueOf(System.currentTimeMillis()), ProfileName.randomName(player.getProfiles()));
        OneBlockAPI.getProfileLoader().createProfile(profile);

        //Create the world and load the world blocks
        OneBlockAPI.getWorldLoader().createWorld(profile.getWorldName());

        //Add the profile to the player and currentPlaying profile
        player.addProfile(profile);

        Loaders.loadProfile(profile, player);
        Logger.player(player.getNoobPlayer(), "one-block.messages.profile-created");

        Logger.player(player.getNoobPlayer(), "one-block.messages.welcome");
        Logger.player(player.getNoobPlayer(), "one-block.messages.break-block-below");
    }
}
