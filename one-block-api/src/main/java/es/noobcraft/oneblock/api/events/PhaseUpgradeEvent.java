package es.noobcraft.oneblock.api.events;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.phases.Phase;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

public class PhaseUpgradeEvent extends Event {
    public static final HandlerList handlers = new HandlerList();

    @Getter private final World world;
    @Getter private final Set<OneBlockProfile> profiles;
    @Getter private final Phase to;

    public PhaseUpgradeEvent(String world, String to) {
        this.world = Bukkit.getWorld(world);
        this.profiles = OneBlockAPI.getProfileCache().getProfiles(this.world.getName());
        this.to = OneBlockAPI.getPhaseLoader().getPhases().stream()
                .filter(phase -> phase.getIdentifier().equals(to)).findFirst()
                .orElse(OneBlockAPI.getPhaseLoader().getPhaseBlocks(world).getPhase());
    }

    /**
     * Inherit from BukkitEvent
     * Manage all the handlers from the BukkitEvent
     * @return a list with all the handler
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Inherit from BukkitEvent
     * Manage all the handlers from the BukkitEvent
     * @return a list with all the handler in a static way
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
