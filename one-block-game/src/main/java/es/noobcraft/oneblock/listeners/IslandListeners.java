package es.noobcraft.oneblock.listeners;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.flags.FlagEncoder;
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

import java.util.BitSet;

public class IslandListeners implements Listener {
    /**
     * Listener to check when a player breaks a block
     * @param event Spigot event
     */
    @EventHandler
    public static void breakBlock(BlockBreakEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.BREAK, true));
    }

    /**
     * Listener to check on when a player places a block
     * @param event Spigot event
     */
    @EventHandler
    public static void placeBlock(BlockPlaceEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.BUILD, true));
    }

    /**
     * Listener to check when a player shears a sheep
     * @param event Spigot event
     */
    @EventHandler
    public static void entitySheared(PlayerShearEntityEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.SHEAR, true));
    }

    /**
     * Listener to check when player picks up a block
     * @param event Spigot event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.LAND_ITEMS, false));
    }

    /**
     * Listener to check when a player drops an item
     * @param event Spigot event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.LAND_ITEMS, true));
    }

    /**
     * Listener to check when a player damages another entity that's not a player
     * @param event Spigot event
     */
    @EventHandler
    public static void entityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (event.getEntity() instanceof Player) return;

        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getDamager().getName());
        event.setCancelled(calculatePerm(player, event.getDamager().getWorld(), IslandFlag.DAMAGE, true));
    }

    /**
     * Listener to check when a player hits another player
     * @param event Spigot event
     */
    @EventHandler
    public static void playerPVP(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) return;
        if (event.getEntity() instanceof Player) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getDamager().getName());
        event.setCancelled(calculatePerm(player, event.getEntity().getWorld(), IslandFlag.PVP, true));
    }

    /**
     * Generic method to calculate if a player can perform an action into an island depending
     * on the island permissions
     * @param player player that performed the action
     * @param world world where the action was done
     * @param islandFlag flag to check
     * @param announce if the cancel action will warn the player
     * @return if the player can perform the action
     */
    private static boolean calculatePerm(OneBlockPlayer player, World world, IslandFlag islandFlag, boolean announce) {
        NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(player.getName());

        //Cancel any listener action into the lobby
        if (world.getName().equals(OneBlockConstants.SPAWN.getWorld().getName())) {
            Logger.player(noobPlayer, "one-block.island.no-perms."+ islandFlag.name().toLowerCase());
            return true;
        }

        //Used to get if the player is in a coop/owner island
        OneBlockProfile playerProfile = OneBlockAPI.getProfileManager().getProfile(
                OneBlockAPI.getProfileCache().getProfiles(world), player);
        //Get perms of the island
        BitSet perms = FlagEncoder.decode(OneBlockAPI.getIslandPermissionLoader().getPermission(world.getName()));

        if (playerProfile == null) {
            if (perms.get(islandFlag.getPos())) {
                if (announce) Logger.player(noobPlayer, "one-block.island.no-perms."+ islandFlag.name().toLowerCase());
                return true;
            }
        }else {
            //Get if the player is not the owner of the profile
            if (!playerProfile.getIslandOwner().equals(player.getName())) {
                //If the is not the owner and permission is set to true cancel the action
                if (perms.get(islandFlag.getPos())) {
                    if (announce) Logger.player(noobPlayer, "one-block.island.no-perms."+ islandFlag.name().toLowerCase());
                    return true;
                }
            }
        }
        return false;
    }

}
