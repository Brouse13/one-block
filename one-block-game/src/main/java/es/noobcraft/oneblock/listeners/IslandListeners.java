package es.noobcraft.oneblock.listeners;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.exceptions.FlagException;
import es.noobcraft.oneblock.api.flags.FlagEncoder;
import es.noobcraft.oneblock.api.flags.FlagType;
import es.noobcraft.oneblock.api.flags.IslandFlag;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class IslandListeners implements Listener {
    @EventHandler
    public static void breakBlock(BlockBreakEvent event) throws FlagException {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.BREAK, true));
    }

    @EventHandler
    public static void placeBlock(BlockPlaceEvent event) throws FlagException {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.BUILD, true));
    }

    @EventHandler
    public static void entitySheared(PlayerShearEntityEvent event) throws FlagException {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.SHEAR, true));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) throws FlagException {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.LAND_ITEMS, false));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) throws FlagException {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.LAND_ITEMS, true));
    }

    @EventHandler
    public static void entityDamage(EntityDamageByEntityEvent event) throws FlagException {
        if (!(event.getDamager() instanceof Player)) return;
        if (event.getEntity() instanceof Player) return;

        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getDamager().getName());
        event.setCancelled(calculatePerm(player, event.getDamager().getWorld(), IslandFlag.DAMAGE, true));
    }

    @EventHandler
    public static void playerPVP(EntityDamageByEntityEvent event) throws FlagException {
        if (event.getDamager() instanceof Player) return;
        if (event.getEntity() instanceof Player) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getDamager().getName());
        event.setCancelled(calculatePerm(player, event.getEntity().getWorld(), IslandFlag.PVP, true));
    }

    private static boolean calculatePerm(OneBlockPlayer player, World world, IslandFlag islandFlag, boolean announce) throws FlagException {
        NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(player.getName());

        if (world.getName().equals(OneBlockConstants.SPAWN.getWorld().getName())) {
            Logger.player(noobPlayer, "one-block.island.no-perms."+ islandFlag.name().toLowerCase());
            return true;
        }

        OneBlockProfile playerProfile = OneBlockAPI.getProfileManager().getProfile(
                OneBlockAPI.getProfileCache().getProfiles(world), player);
        char[] perms = FlagEncoder.decode(OneBlockAPI.getIslandPermissionLoader().getPermission(world.getName()));

        FlagType type = FlagEncoder.getType(islandFlag, perms);

        if (playerProfile == null) {
            if (type != FlagType.ALL) {
                if (announce) Logger.player(noobPlayer, "one-block.island.no-perms."+ islandFlag.name().toLowerCase());
                return true;
            }
        }else {
            if (playerProfile.getProfileName().equals(player.getName()) && type == FlagType.OWNER) {
                if (announce) Logger.player(noobPlayer, "one-block.island.no-perms."+ islandFlag.name().toLowerCase());
                return true;
            }
        }
        return false;
    }

}
