package es.noobcraft.oneblock.listeners;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.events.InfiniteBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class PhaseListeners implements Listener {
    private final JavaPlugin plugin;

    public PhaseListeners(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getWorld().getName().equals("lobby")) {
            event.setCancelled(true);
            return;
        }

        if (event.getBlock().getLocation().toVector().equals(OneBlockAPI.getSettings().getIslandSpawn())) {
            event.setCancelled(true);

            Location location = event.getPlayer().getLocation();
            Vector infBlock = OneBlockAPI.getSettings().getIslandSpawn().clone();
            if (infBlock.getX() == ((int) location.getX()) && infBlock.getZ() == ((int) location.getZ())) {
                location.setY(infBlock.getBlockY()+ 1);
                event.getPlayer().teleport(location);
            }



            //If item is a tool damage it
            ItemMeta itemMeta = event.getPlayer().getInventory().getItemInHand().getItemMeta();
            if (itemMeta instanceof Damageable) ((Damageable) itemMeta).damage(1);

            InfiniteBlockBreakEvent infiniteBlockEvent = new InfiniteBlockBreakEvent(
                    OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName()),
                    event.getBlock().getWorld().getName(),
                    OneBlockAPI.getPhaseLoader().getPhaseBlocks(event.getBlock().getWorld().getName()),
                    event.getBlock());
            Bukkit.getServer().getScheduler().runTaskLater(plugin, ()-> Bukkit.getPluginManager().callEvent(infiniteBlockEvent), 2L);
            event.getBlock().getState().update(true, true);
        }
    }
}
