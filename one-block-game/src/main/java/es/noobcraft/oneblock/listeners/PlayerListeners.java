package es.noobcraft.oneblock.listeners;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.event.AsyncNoobPlayerPreLoginEvent;
import es.noobcraft.core.api.event.NoobPlayerJoinEvent;
import es.noobcraft.core.api.event.NoobPlayerQuitEvent;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.loaders.PlayerLoader;
import es.noobcraft.oneblock.scoreboard.LobbyScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListeners implements Listener {
    private final Translator translator = Core.getTranslator();

    @EventHandler(ignoreCancelled = true)
    public void onAsyncNoobPlayerPreLogin(AsyncNoobPlayerPreLoginEvent event) {
        if (!event.getResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED))
            return;
        OneBlockAPI.getPlayerCache().addPlayer(event.getPlayer().getUsername());

        final OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getUsername());
        player.setProfiles(OneBlockAPI.getProfileLoader().getProfiles(player));
        player.getProfiles().forEach(profile -> {
            OneBlockAPI.getProfileCache().addProfile(profile);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onNoobPlayerJoin(NoobPlayerJoinEvent event) {
        final OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getNoobPlayer().getUsername());
        OneBlockAPI.getScoreboardManager().createScoreboard(player, new LobbyScoreBoard(player));

        event.getNoobPlayer().teleport(OneBlockConstants.SPAWN);

        SpigotCore.getChatManager().setPerWorld(true);
        event.getNoobPlayer().getInventory().setItem(0, SpigotCore.getImmutableItemManager().makeImmutable(
                ItemBuilder.from(Material.NETHER_STAR)
                        .displayName(translator.getLegacyText(event.getNoobPlayer(), ""))
                        .lore(translator.getLegacyTextList(event.getNoobPlayer(), ""))
                        .metadata("event", "profile-list").build()));
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

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName());
        if (player.getCurrentProfile() != null) {
            final World world = Bukkit.getWorld(player.getCurrentProfile().getProfileName());
            if (world.getBlockAt(new Location(world, 0, 30, 0)).getType() == Material.AIR)
                world.getBlockAt(new Location(world, 0, 30, 0)).setType(Material.GRASS);
            event.setRespawnLocation(new Location(world, 0, 30, 0));
        }
        event.getPlayer().teleport(OneBlockConstants.SPAWN);
    }
}
