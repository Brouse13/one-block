package es.noobcraft.oneblock.player;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.player.PlayerCache;

import java.util.Set;

public class SetPlayerCache implements PlayerCache {
    private final Set<OneBlockPlayer> players = Sets.newHashSet();

    @Override
    public Set<OneBlockPlayer> getPlayers() {
        return ImmutableSet.copyOf(players);
    }

    @Override
    public OneBlockPlayer getPlayer(String name) {
        return players.stream().filter(oneBlockPlayer -> oneBlockPlayer.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public boolean addPlayer(OneBlockPlayer player) {
        return players.add(player);
    }

    @Override
    public boolean removePlayer(OneBlockPlayer player) {
        return players.remove(player);
    }
}
