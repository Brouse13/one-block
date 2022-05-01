package es.noobcraft.oneblock.listeners;

import com.google.common.collect.Sets;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.events.InfiniteBlockBreakEvent;
import es.noobcraft.oneblock.api.events.PhaseUpgradeEvent;
import es.noobcraft.oneblock.api.phases.Phase;
import es.noobcraft.oneblock.api.phases.PhaseBlocks;
import es.noobcraft.oneblock.api.phases.PhaseGenerators;
import es.noobcraft.oneblock.api.phases.SpecialActions;
import es.noobcraft.oneblock.phase.BaseBlockType;
import es.noobcraft.oneblock.phase.BaseLootTable;
import es.noobcraft.oneblock.phase.BaseMobType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class InfiniteBlockListener implements Listener {
    private final Set<SpecialActions.Actions> locked = Sets.newHashSet(SpecialActions.Actions.BLOCK, SpecialActions.Actions.LOOT_TABLE, SpecialActions.Actions.UPGRADE);
    private final Function<List<?>, Integer> random = list -> ((int) (Math.random() * list.size()));

    @EventHandler(ignoreCancelled = true)
    public void onInfiniteBlockBreak(InfiniteBlockBreakEvent event) {
        Block block = event.getBlock();
        PhaseBlocks phaseblocks = event.getPhaseblocks();
        boolean generate = true;
        World world = event.getBlock().getWorld();

        //Spawn the block drops
        final Collection<ItemStack> drops = block.getDrops(event.getPlayer().getBukkitPlayer().getItemInHand());
        drops.forEach(itemStack -> block.getWorld().dropItemNaturally(
                        block.getLocation().add(new Vector(0.5, 1, 0.5)), itemStack)
                .setVelocity(new Vector(0, 0, 0)));

        //Get all the SpecialActions that will be done on that block
        if (phaseblocks.getPhase().getSpecialActions().containsKey(phaseblocks.getBlocks())) {
            for (SpecialActions action : phaseblocks.getPhase().getSpecialActions().get(phaseblocks.getBlocks())) {
                switch (action.getAction()) {
                    case MOB:
                        OneBlockAPI.getGson().fromJson(action.getValue(), BaseMobType.class).summon(event.getPlayer());
                    break;
                    case BLOCK:
                        OneBlockAPI.getGson().fromJson(action.getValue(), BaseBlockType.class).spawn(world);
                    break;
                    case MESSAGE:
                        PhaseGenerators.generateMessage(event.getPlayer(), action.getValue());
                    break;
                    case ACTIONBAR:
                        PhaseGenerators.generateActionBar(event.getPlayer(), action.getValue());
                    break;
                    case LOOT_TABLE:
                        OneBlockAPI.getGson().fromJson(action.getValue(), BaseLootTable.class).summon(world);
                    break;
                    case TITLE:
                        PhaseGenerators.generateTitle(event.getPlayer(), action.getValue());
                    break;
                    case UPGRADE:
                        PhaseUpgradeEvent phaseUpgradeEvent = new PhaseUpgradeEvent(event.getWorld(), action.getValue());
                        Bukkit.getServer().getPluginManager().callEvent(phaseUpgradeEvent);
                        break;
                }
                generate = !generate || !locked.contains(action.getAction());
            }
        }

        //If SpecialActions hasn't performed any locked actions
        if (generate) {
            Phase phase = phaseblocks.getPhase();

            double type = Math.random();
            if (type < .05) { //LootTable generation
                if (phase.getLootTables().size() != 0) {
                    phase.getLootTables().get(random.apply(phase.getLootTables())).summon(world);
                }else {
                    phase.getItems().get(random.apply(phase.getItems())).spawn(world);
                }
            }else if (type < .15) {//Entities generation
                if (phase.getEntities().size() != 0) {
                    phase.getEntities().get(random.apply(phase.getEntities())).summon(event.getPlayer());
                }else {
                    phase.getItems().get(random.apply(phase.getItems())).spawn(world);
                }
            }else {//Generate a random block
                phase.getItems().get(random.apply(phase.getItems())).spawn(world);
            }
        }

        //Add one block to the phase and set the block to grass
        phaseblocks.addBlock(1);
        block.getState().update(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChange(EntityChangeBlockEvent event) {
        //Only trigger the action if the block is on the InfiniteBlock location and is a falling block
        if(!event.getBlock().getLocation().toVector().equals(OneBlockAPI.getSettings().getIslandSpawn())) return;
        if (!(event.getEntity() instanceof FallingBlock) || event.getTo() != Material.AIR) return;

        //Generate a new block
        event.setCancelled(true);

        Phase phase = OneBlockAPI.getPhaseLoader().getPhaseBlocks(event.getBlock().getWorld().getName()).getPhase();
        phase.getItems().get(random.apply(phase.getItems())).spawn(event.getBlock().getWorld());
    }
}
