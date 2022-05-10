package es.noobcraft.oneblock.utils;

import com.google.common.collect.Maps;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.stream.Collectors;

public class InviteManager {
    private static BukkitTask taskId;
    private static final Map<Long, Entry> expires = Maps.newHashMap();

    public static void addPlayer(String player, OneBlockProfile profile) {
        //We add a random number between 0 and 10 to avoid two simultaneous task ends
        expires.put((long) (System.currentTimeMillis()+ 60 * 1000+ (Math.random() * 10)),
                new Entry(player, profile));
    }

    public static void removePlayer(String player) {
        for (Long expire : expires.keySet()) {
            if (expires.get(expire).getPlayer().equals(player)) {
                expires.remove(expire);
                return;
            }
        }
    }

    public static boolean hasInvitations(String player) {
        return expires.values().stream().map(Entry::getPlayer).anyMatch(player::equals);
    }


    public static OneBlockProfile getProfile(String player) {
        for (Entry value : expires.values()) {
            if (value.getPlayer().equals(player)) {
                return value.getProfile();
            }
        }
        return null;
    }

    public static void schedule(JavaPlugin plugin, boolean force) {
        if (force) {
            taskId.cancel();
            taskId = null;
        }

        if (taskId != null) return;

        taskId = Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (expires.isEmpty()) return;
            long time = System.currentTimeMillis();

            for (Long expire : expires.keySet().stream().filter(expire -> time >= expire).collect(Collectors.toSet())) {
                OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(expires.get(expire).getPlayer());
                if (player != null)
                    Logger.player(player.getNoobPlayer(), "one-block.messages.invite.expire");
                expires.remove(expire);
            }
        }, 0L, 20L);
    }

    @AllArgsConstructor
    static class Entry {
        @Getter private final String player;
        @Getter private final OneBlockProfile profile;
    }
}
