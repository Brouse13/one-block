package es.noobcraft.oneblock.listeners;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.events.InfiniteBlockBreakEvent;
import es.noobcraft.oneblock.api.events.PhaseUpgradeEvent;
import es.noobcraft.oneblock.api.phases.Phase;
import es.noobcraft.oneblock.api.phases.PhaseBlocks;
import es.noobcraft.oneblock.api.phases.SpecialActions;
import es.noobcraft.oneblock.api.phases.generators.PhaseGenerators;
import es.noobcraft.oneblock.phase.BaseLootTable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class InfiniteBlockListener implements Listener {
    private final Set<SpecialActions.Actions> locked = Sets.newHashSet(SpecialActions.Actions.BLOCK, SpecialActions.Actions.LOOT_TABLE, SpecialActions.Actions.UPGRADE);

    @EventHandler(ignoreCancelled = true)
    public void onInfiniteBlockBreak(InfiniteBlockBreakEvent event) {
        Block block = event.getBlock();
        PhaseBlocks phaseblocks = event.getPhaseblocks();
        boolean generate = true;

        //Spawn the block drops
        final Collection<ItemStack> drops = block.getDrops(event.getPlayer().getBukkitPlayer().getItemInHand());
        drops.forEach(itemStack -> block.getWorld().dropItemNaturally(
                        block.getLocation().add(new Vector(0.5, 1, 0.5)), itemStack)
                .setVelocity(new Vector(0, 0, 0)));

        //Get all the SpecialActions that will be done on that block
        if (phaseblocks.getPhase().getSpecialActions().containsKey(phaseblocks.getBlocks())) {
            for (SpecialActions action : phaseblocks.getPhase().getSpecialActions().get(phaseblocks.getBlocks())) {
                switch (action.getAction()) {
                    case MOB: PhaseGenerators.generateEntity(block,
                            Collections.singletonList(EntityType.valueOf(action.getValue()))).generate();
                    break;
                    case BLOCK: PhaseGenerators.generateBlock(block,
                            Collections.singletonList(itemBuilder(action.getValue().split(":")))).generate();
                    break;
                    case MESSAGE: PhaseGenerators.generateMessage(event.getPlayer(), action.getValue()).generate();
                    break;
                    case ACTIONBAR: PhaseGenerators.generateActionBar(event.getPlayer(), action.getValue()).generate();
                    break;
                    case LOOT_TABLE:
                        PhaseGenerators.generateLootTable(event.getBlock(), Collections.singletonList(Arrays.asList(
                                    OneBlockAPI.getGson().fromJson(action.getValue(), BaseLootTable[].class)))).generate();
                    break;
                    case TITLE: PhaseGenerators.generateTitle(event.getPlayer(), action.getValue()).generate();
                    break;
                    case UPGRADE:
                        PhaseUpgradeEvent phaseUpgradeEvent = new PhaseUpgradeEvent(event.getWorld(), phaseblocks.getPhase());
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
                if (phase.getLootTables().size() != 0)
                    PhaseGenerators.generateLootTable(block, phase.getLootTables()).generate();
                else
                    PhaseGenerators.generateBlock(block, phase.getItems()).generate();
            }else if (type < .15) {//Entities generation
                if (phase.getEntities().size() != 0)
                    PhaseGenerators.generateEntity(block, phase.getEntities()).generate();
                PhaseGenerators.generateBlock(block, phase.getItems()).generate();
            }
            else //Generate a random block
                PhaseGenerators.generateBlock(block, phase.getItems()).generate();
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
        PhaseGenerators.generateBlock(event.getBlock(),
                OneBlockAPI.getPhaseLoader().getPhaseBlocks(event.getBlock().getWorld().getName()).getPhase().getItems()).generate();
    }

    private ItemStack itemBuilder(String[] item) {
        ItemBuilder itemBuilder = ItemBuilder.from(Material.valueOf(item[0]));
        if (item.length > 1) itemBuilder.damage(Integer.parseInt(item[1]));

        return itemBuilder.build();
    }
}
