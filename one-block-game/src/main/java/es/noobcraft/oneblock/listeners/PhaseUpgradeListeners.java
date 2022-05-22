package es.noobcraft.oneblock.listeners;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.events.PhaseUpgradeEvent;
import es.noobcraft.oneblock.schedulers.PhaseUpgradeScheduler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.concurrent.CompletableFuture;

public class PhaseUpgradeListeners implements Listener {
    private final JavaPlugin plugin;

    public PhaseUpgradeListeners(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPhaseUpgrade(PhaseUpgradeEvent event) {
        //Get the infinite block and set it to Bedrock
        Block block = event.getWorld().getBlockAt(OneBlockAPI.getSettings().getIslandSpawn().toLocation(event.getWorld()));
        block.setType(Material.BEDROCK);
        block.getState().update(true);

        //Create an armour stand on the infinite block
        ArmorStand armorStand = event.getWorld().spawn(
                OneBlockAPI.getSettings().getIslandSpawn().clone().add(new Vector(.5, 0, .5)).toLocation(event.getWorld()),
                ArmorStand.class);

        //Call the Upgrade phase scheduler
        CompletableFuture.runAsync(new PhaseUpgradeScheduler(armorStand, plugin, () -> {//Scheduler will run sync
            //Kill ArmourStand
            armorStand.remove();

            //Spawn the block
            event.getTo().getItems().getRandom().spawn(event.getWorld());

            //Upgrade the phase
            OneBlockAPI.getPhaseLoader().getPhaseBlocks(event.getWorld().getName()).setPhase(event.getTo());
        }));
    }
}
