package es.noobcraft.oneblock.listeners;

import es.noobcraft.oneblock.api.OneBlockAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class LobbyListeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(event.getWorld().getName().equals("lobby"));
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(event.getEntity().getWorld().getName().equals("lobby"));
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        event.setCancelled(event.getEntity().getWorld().getName().equals("lobby"));
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        event.setCancelled(event.getEntity().getWorld().getName().equals("lobby"));
        //If player height is lower to 30 player is falling, teleport to lobby
        if (event.getEntity().getLocation().getY() <= 30)
            event.getEntity().teleport(OneBlockAPI.getSettings().getLobbySpawn());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        event.setCancelled(event.getBlock().getWorld().getName().equals("lobby"));
    }

    @EventHandler(ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(event.getBlock().getWorld().getName().equals("lobby"));
    }
}
