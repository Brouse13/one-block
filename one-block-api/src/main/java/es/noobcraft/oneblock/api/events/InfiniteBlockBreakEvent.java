package es.noobcraft.oneblock.api.events;

import es.noobcraft.oneblock.api.phases.PhaseBlocks;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InfiniteBlockBreakEvent extends Event {
    public static final HandlerList handlers = new HandlerList();

    @Getter private final OneBlockPlayer player;
    @Getter private final String world;
    @Getter private final PhaseBlocks phaseblocks;
    @Getter private final Block block;

    public InfiniteBlockBreakEvent(OneBlockPlayer player, String world, PhaseBlocks phaseBlocks, Block block) {
        this.player = player;
        this.world = world;
        this.phaseblocks = phaseBlocks;
        this.block = block;
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
