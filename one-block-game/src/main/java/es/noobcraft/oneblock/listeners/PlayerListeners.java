package es.noobcraft.oneblock.listeners;

import es.noobcraft.core.api.event.AsyncNoobPlayerPreLoginEvent;
import es.noobcraft.core.api.event.NoobPlayerQuitEvent;
import es.noobcraft.oneblock.api.OneBlockAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onAsyncNoobPlayerPreLogin(AsyncNoobPlayerPreLoginEvent event) {
        if (event.getResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED))
            OneBlockAPI.getPlayerCache().addPlayer(event.getPlayer().getUsername());
    }

    @EventHandler(ignoreCancelled = true)
    public void onNoobPlayerQuit(NoobPlayerQuitEvent event) {
        OneBlockAPI.getPlayerCache().removePlayer(event.getNoobPlayer().getName());
    }
}
