package es.noobcraft.oneblock.listeners;

import es.noobcraft.core.api.event.AsyncNoobPlayerPreLoginEvent;
import es.noobcraft.core.api.event.NoobPlayerQuitEvent;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Set;


public class PlayerListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onAsyncNoobPlayerPreLogin(AsyncNoobPlayerPreLoginEvent event) {
        if (event.getResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED))
            OneBlockAPI.getPlayerCache().addPlayer(event.getPlayer().getUsername());

        final OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getUsername());
        final Set<OneBlockProfile> profiles = OneBlockAPI.getProfileLoader().getProfiles(player);
        profiles.forEach(profile -> OneBlockAPI.getProfileCache().addProfile(profile));
        System.out.println("Loaded profiles from "+ event.getPlayer().getUsername());
    }

    @EventHandler(ignoreCancelled = true)
    public void onNoobPlayerQuit(NoobPlayerQuitEvent event) {
        OneBlockAPI.getPlayerCache().removePlayer(event.getNoobPlayer().getName());
    }
}
