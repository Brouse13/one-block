package es.noobcraft.oneblock.utils;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.inventory.InventorySerializer;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.profile.ProfileName;
import es.noobcraft.oneblock.profile.BaseOneBlockProfile;
import es.noobcraft.oneblock.scoreboard.IslandScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Loaders {
    public static void loadProfile(OneBlockProfile profile, OneBlockPlayer player) {
        OneBlockAPI.getWorldLoader().loadWorld(profile.getWorldName(), false);
        player.setCurrentProfile(profile);
        OneBlockAPI.getPhaseLoader().getPhaseBlocks(profile.getWorldName());
        OneBlockAPI.getScoreboardManager().createScoreboard(player, new IslandScoreBoard(profile));

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

        inventory.setItem(0, SpigotCore.getImmutableItemManager().makeMutable(inventory.getItem(0)));
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

        //Encode current player inventory
        List<ItemStack> inventory = new ArrayList<>();
        inventory.addAll(Arrays.asList(bukkitPlayer.getInventory().getArmorContents()));
        inventory.addAll(Arrays.asList(bukkitPlayer.getInventory().getContents()));

        profile.setInventory(InventorySerializer.serialize(inventory.toArray(new ItemStack[0])));

        OneBlockAPI.getServerLoader().removeWorld(Core.getServerId(), profile.getWorldName());
        OneBlockAPI.getProfileLoader().updateProfile(profile);
        bukkitPlayer.getInventory().clear();
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
