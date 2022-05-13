package es.noobcraft.oneblock.utils;

import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.utils.CacheMap;

public class InviteManager {
    private static final CacheMap<String, OneBlockProfile> cached = new CacheMap<>(2 * 60 * 1000L);

    public static void addPlayer(String player, OneBlockProfile profile) {
        cached.put(player, profile);
    }

    public static void removePlayer(String player) {
        cached.remove(player);
    }

    public static boolean hasInvitations(String player) {
        return cached.containsKey(player);
    }


    public static OneBlockProfile getProfile(String player) {
        return cached.get(player);
    }
}
