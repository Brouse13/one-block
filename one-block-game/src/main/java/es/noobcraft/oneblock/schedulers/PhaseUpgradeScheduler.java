package es.noobcraft.oneblock.schedulers;

import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.phases.generators.Generate;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

public class PhaseUpgradeScheduler {
    private final ArmorStand armorStand;
    private final Generate generate;

    public PhaseUpgradeScheduler(ArmorStand armorStand, Generate generate) {
        this.armorStand = armorStand;
        this.generate = generate;
    }

    public Runnable getScheduler(JavaPlugin plugin) {
        return () -> {
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
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                armorStand.remove();
                generate.generate();
            }, 2L);
        };
    }
}
