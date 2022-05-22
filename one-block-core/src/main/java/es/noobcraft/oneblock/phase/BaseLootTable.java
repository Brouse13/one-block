package es.noobcraft.oneblock.phase;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.phases.LootTable;
import es.noobcraft.oneblock.api.phases.LootTableItem;
import es.noobcraft.oneblock.api.utils.WeighList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

@Builder
@AllArgsConstructor
public class BaseLootTable implements LootTable {
    @Getter private final String name;
    @Getter private final WeighList<LootTableItem> items;
    @Getter private final int rolls;

    @Override
    public void summon(World world) {
        //Spawn the chest with the custom name
        Block block = world.getBlockAt(OneBlockAPI.getSettings().getIslandSpawn().toLocation(world));
        new BaseBlockType(new ItemStack(Material.CHEST), 1).spawn(world);

        Chest chest = ((Chest) block.getState());

        for (int i = 0; i < rolls; i++)
            chest.getBlockInventory().addItem(items.getRandom().getItem());

        chest.update();
    }
}
