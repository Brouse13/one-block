package es.noobcraft.oneblock.listeners;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.permission.FlagEncoder;
import es.noobcraft.oneblock.api.permission.IslandFlag;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.BitSet;

public class IslandListeners implements Listener {
    /**
     * Listener to check when a player breaks a block
     * @param event Spigot event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public static void breakBlock(BlockBreakEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.BREAK, true));
    }

    /**
     * Listener to check on when a player places a block
     * @param event Spigot event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public static void placeBlock(BlockPlaceEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.BUILD, true));
    }

    /**
     * Listener to check when a player shears a sheep
     * @param event Spigot event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public static void entitySheared(PlayerShearEntityEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.SHEAR, true));
    }

    /**
     * Listener to check when player picks up a block
     * @param event Spigot event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.LAND_ITEMS, false));
    }

    /**
     * Listener to check when a player drops an item
     * @param event Spigot event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.LAND_ITEMS, true));
    }

    /**
     * Listener to check when a player damages another entity that's not a player
     * @param event Spigot event
     */
    @EventHandler(priority = EventPriority.LOWEST)
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
    @EventHandler(priority = EventPriority.LOWEST)
    public static void playerPVP(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

        BitSet permission = FlagEncoder.decode(OneBlockAPI.getPermissionManager().getPermission(event.getDamager().getWorld().getName()));
        event.setCancelled(permission.get(IslandFlag.PVP.getIndex()));
    }

    /**
     * Listener to check if player interacted with a container
     * @param event Spigot event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void chestInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (!(event.getClickedBlock().getState() instanceof InventoryHolder)) return;

        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        event.setCancelled(calculatePerm(player, event.getPlayer().getWorld(), IslandFlag.INVENTORIES, true));
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
        //Cancel any action in the lobby
        if (world.getName().equals(OneBlockAPI.getSettings().getLobbySpawn().getWorld().getName())) {
            Logger.player(player.getNoobPlayer(), "one-block.island.no-perms.lobby");
            return true;
        }

        //Get island permission on a BitSet
        BitSet permission = FlagEncoder.decode(OneBlockAPI.getPermissionManager().getPermission(world.getName()));
        boolean visitor = player.getCurrentProfile() == null;

        /*-------------------*
         | TYPE     | 1  | 0 |
         | MEMBERS  | Y  | Y |
         | VISITORS | N  | Y |
         *-------------------*/
        //If BitSet == true only members can perform, otherwise, all players can
        if (visitor && permission.get(islandFlag.getIndex())) {
            if (announce)
                Logger.player(player.getNoobPlayer(), "one-block.island.no-perms."+ islandFlag.name().toLowerCase());
            return true;
        }
        return false;
    }

}
