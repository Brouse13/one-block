package es.noobcraft.oneblock.player;


import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.scoreboard.OneBlockScoreBoard;
import es.noobcraft.oneblock.scoreboard.BaseScoreboardManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Set;


public class BaseOneBlockPlayer implements OneBlockPlayer {
    @Getter private final String name;
    @Getter private final int maxProfiles;
    @Getter @Setter private OneBlockProfile currentProfile;
    @Getter private OneBlockScoreBoard scoreBoard;

    private final Set<OneBlockProfile> profiles = Sets.newHashSet();

    public BaseOneBlockPlayer(String name, int maxProfiles) {
        this.name = name;
        this.maxProfiles = maxProfiles;
    }

    @Override
    public Set<OneBlockProfile> getProfiles() {
        return ImmutableSet.copyOf(profiles);
    }

    @Override
    public void addProfile(OneBlockProfile profile) {
        if (profiles.size() < maxProfiles)
            profiles.add(profile);
    }

    @Override
    public void removeProfile(OneBlockProfile profile) {
        profiles.remove(profile);
    }

    @Override
    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(name);
    }

    @Override
    public NoobPlayer getNoobPlayer() {
        return Core.getPlayerCache().getPlayer(name);
    }

    @Override
    public boolean isVip() {
        return getNoobPlayer().getSetGroups().contains(Group.SUPPORTER);
    }

    @Override
    public void setScoreBoard(OneBlockScoreBoard scoreBoard) {
        if (scoreBoard == null) {
            BaseScoreboardManager.removeScoreBoard(this);
            this.scoreBoard = null;
        }
        else  {
            BaseScoreboardManager.addScoreboard(this, scoreBoard);
            this.scoreBoard = scoreBoard;
        }
    }

    @Override
    public void teleport(OneBlockProfile profile) {
        //Load the world if it isn't loaded
        OneBlockAPI.getWorldLoader().loadWorld(profile.getWorldName(), false);

        //Get world and spawn location
        World world = Bukkit.getWorld(profile.getWorldName());
        Location location = new Location(world, 0, 50, 0);

        //Set the spawn block to GRASS if is not set
        if (world.getBlockAt(location).getType().equals(Material.AIR))
            world.getBlockAt(location).setType(Material.GRASS);

        //Teleport the player to the region
        getBukkitPlayer().teleport(location.add(new Vector(0, 1, 0)));
    }
}
