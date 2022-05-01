package es.noobcraft.oneblock.schedulers;

import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

public class PhaseUpgradeScheduler implements Runnable {
    private final ArmorStand armorStand;
    private final JavaPlugin plugin;
    private final Runnable runnable;

    public PhaseUpgradeScheduler(ArmorStand armorStand, JavaPlugin plugin, Runnable runnable) {
        this.armorStand = armorStand;
        this.plugin = plugin;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        //Set the properties to the ArmourStand
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);

        //Log the upgrade on the ArmourStand DisplayName
        for (int i = 0; i < 10; i++) {
            armorStand.setCustomName("§a" +(10- i));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                Logger.log(LoggerType.ERROR, exception.getMessage());
            }
        }
        Bukkit.getServer().getScheduler().runTaskLater(plugin, runnable, 2L);
    }
}
