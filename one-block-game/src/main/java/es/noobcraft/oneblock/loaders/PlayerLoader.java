package es.noobcraft.oneblock.loaders;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.inventory.InventorySerializer;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PlayerLoader {
    public static void loadPlayer(OneBlockPlayer player, OneBlockProfile profile) {
        NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(player.getName());
        ItemStack[] deserialize;
        try {
            deserialize = InventorySerializer.deserialize(profile.getInventory());
        } catch (IOException e) {
            e.printStackTrace();
            Logger.player(noobPlayer, "one-block.island.error.inventory-deserialize");
            return;
        }

        //Load player inv and teleport to default block
        Player bukkitPlayer = Bukkit.getPlayer(player.getName());
        PlayerInventory inventory = bukkitPlayer.getInventory();

        inventory.setItem(0, SpigotCore.getImmutableItemManager().makeMutable(inventory.getItem(0)));
        inventory.clear();

        if (deserialize.length != 0) {
            inventory.setArmorContents(Arrays.copyOfRange(deserialize, 0, 4));
            inventory.setContents(Arrays.copyOfRange(deserialize, 4, deserialize.length));
        }

        bukkitPlayer.teleport(new Location(Bukkit.getWorld(profile.getProfileName()), 0, 31, 0));

        Logger.player(noobPlayer, "one-block.island.teleport-island");
    }

    public static void unloadPlayer(OneBlockPlayer oneBlockPlayer, OneBlockProfile profile) {
        Player bukkitPlayer = Bukkit.getPlayer(oneBlockPlayer.getName());

        //Encode current player inventory
        List<ItemStack> inventory = new ArrayList<>();
        inventory.addAll(Arrays.asList(bukkitPlayer.getInventory().getArmorContents()));
        inventory.addAll(Arrays.asList(bukkitPlayer.getInventory().getContents()));

        profile.setInventory(InventorySerializer.serialize(inventory.toArray(new ItemStack[0])));

        OneBlockAPI.getProfileLoader().updateProfile(profile);
        bukkitPlayer.getInventory().clear();
    }
}
