package es.noobcraft.oneblock.player;

import com.google.common.collect.Sets;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.player.PlayerCache;

import java.util.Collections;
import java.util.Set;

public class SetOneBlockPlayerCache implements PlayerCache {
    private final Set<OneBlockPlayer> players = Sets.newHashSet();

    @Override
    public Set<OneBlockPlayer> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    @Override
    public OneBlockPlayer getPlayer(String name) {
        return players.stream().filter(oneblockPlayer -> oneblockPlayer.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public boolean addPlayer(String name) {
        return players.add(new BaseOneBlockPlayer(name));
    }

    @Override
    public boolean removePlayer(String name) {
        return players.remove(getPlayer(name));
    }
}
