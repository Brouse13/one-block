package es.noobcraft.oneblock.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;

public class PhaseListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final Collection<ItemStack> drops = event.getBlock().getDrops(event.getPlayer().getItemInHand());

        if (event.getBlock().getLocation().toVector().equals(new Vector(0, 30, 0)) &&
            !event.getBlock().getWorld().getName().equals("lobby")) {
            event.setCancelled(true);

            drops.forEach(itemStack -> event.getBlock().getWorld().dropItemNaturally(
                    event.getBlock().getLocation().add(new Vector(0, 1, 0)), itemStack));

            event.getBlock().setType(Material.GRASS);
        }
    }
}
