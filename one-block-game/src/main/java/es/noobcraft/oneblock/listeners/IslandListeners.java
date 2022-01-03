package es.noobcraft.oneblock.listeners;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.logger.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.util.Set;

public class IslandListeners implements Listener {
    //TODO create all the methods of the listeners
    @EventHandler
    public static void breakBlock(BlockBreakEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(player.getName());
        Set<OneBlockProfile> profiles = OneBlockAPI.getProfileCache().getProfiles(event.getBlock().getWorld());

        if (profiles.isEmpty()) return;

        if (!OneBlockAPI.getProfileManager().canBuild(profiles, player)) {
            event.setCancelled(true);
            Logger.player(noobPlayer, "one-block.island.no-perms.break");
        }
    }

    @EventHandler
    public static void placeBlock(BlockPlaceEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(player.getName());
        Set<OneBlockProfile> profiles = OneBlockAPI.getProfileCache().getProfiles(event.getBlock().getWorld());

        if (profiles.isEmpty()) return;

        if (!OneBlockAPI.getProfileManager().canBuild(profiles, player)) {
            event.setCancelled(true);
            Logger.player(noobPlayer, "one-block.island.no-perms.place");
        }
    }

    @EventHandler
    public static void entitySheared(PlayerShearEntityEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(player.getName());
        Set<OneBlockProfile> profiles = OneBlockAPI.getProfileCache().getProfiles(event.getEntity().getWorld());

        if (profiles.isEmpty()) return;

        if (!OneBlockAPI.getProfileManager().canBuild(profiles, player)) {
            event.setCancelled(true);
            Logger.player(noobPlayer, "one-block.island.no-perms.shear");
        }
    }

    @EventHandler
    public static void entityDamaged(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getDamager().getName());
        NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(player.getName());
        Set<OneBlockProfile> profiles = OneBlockAPI.getProfileCache().getProfiles(event.getEntity().getLocation().getWorld());

        if (profiles.isEmpty()) return;

        if (!OneBlockAPI.getProfileManager().canBuild(profiles, player)) {
            event.setCancelled(true);
            Logger.player(noobPlayer, "one-block.island.no-perms.damage");
        }
    }
}
