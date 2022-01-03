package es.noobcraft.oneblock.listeners;

import es.noobcraft.core.api.event.AsyncNoobPlayerPreLoginEvent;
import es.noobcraft.core.api.event.NoobPlayerQuitEvent;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.loaders.PlayerLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onAsyncNoobPlayerPreLogin(AsyncNoobPlayerPreLoginEvent event) {
        if (!event.getResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED))
            return;
        OneBlockAPI.getPlayerCache().addPlayer(event.getPlayer().getUsername());

        final OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getUsername());
        player.setProfiles(OneBlockAPI.getProfileLoader().getProfiles(player));
    }

    @EventHandler(ignoreCancelled = true)
    public void onNoobPlayerQuit(NoobPlayerQuitEvent event) {
        OneBlockPlayer oneBlockPlayer = OneBlockAPI.getPlayerCache().getPlayer(event.getNoobPlayer().getName());

        Bukkit.getPlayer(oneBlockPlayer.getName())
                .teleport(new Location(Bukkit.getWorld("lobby"), 0, 50, 0));

        oneBlockPlayer.getProfiles().forEach(profile -> {
            PlayerLoader.unloadPlayer(oneBlockPlayer, profile);
            OneBlockAPI.getProfileCache().removeProfile(profile);
            OneBlockAPI.getWorldManager().unloadWorld(profile.getProfileName());
        });
        OneBlockAPI.getPlayerCache().removePlayer(oneBlockPlayer.getName());

    }
}
