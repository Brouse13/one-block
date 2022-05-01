package es.noobcraft.oneblock.phase;

import com.google.common.collect.Lists;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.phases.LootTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class BaseLootTable implements LootTable {
    @Getter private final String name;
    @Getter private final List<LootTableItem> items;
    @Getter private final int rolls;

    @Override
    public void summon(World world) {
        //Spawn the chest with the custom name
        Block block = world.getBlockAt(OneBlockAPI.getSettings().getIslandSpawn().toLocation(world));
        ItemStack itemStack = ItemBuilder.from(Material.CHEST).displayName(name).build();
        block.getState().setType(itemStack.getType());
        block.getState().setData(itemStack.getData());
        block.getState().update();

        Chest chest = ((Chest) block.getState());

        for (int i = 0; i < rolls; i++)
            chest.getBlockInventory().addItem(getRandom());

        chest.update();
    }

    public static BaseLootTableBuilder builder() {
        return new BaseLootTableBuilder();
    }

    private ItemStack getRandom() {
        double random = Math.random();
        for (int i = 0; i < items.size() - 1; i++)
            if (items.get(i + 1).getChance() > random)
                return items.get(i).getItem();
        return items.get(items.size() - 1).getItem();
    }

    public static class BaseLootTableBuilder {
        private int totalWeigh = 0;
        protected final List<LootTableItem> entries;
        protected String name;
        protected int rolls;

        protected BaseLootTableBuilder() {
            entries = Lists.newArrayList();
        }

        public BaseLootTableBuilder addItem(LootTableItem item) {
            entries.add(item);
            totalWeigh += item.getWeigh();
            return this;
        }

        public BaseLootTableBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BaseLootTableBuilder rolls(int rolls) {
            this.rolls = rolls;
            return this;
        }

        public LootTable build() {
            if (entries.size() <= 0 && totalWeigh <= 0) throw new NullPointerException("Items mustn't be null");

            double base = 0;
            for (LootTableItem entry : entries) {
                double chance = base / totalWeigh;
                entry.setChance(chance);
                base += entry.getWeigh();
            }

            return new BaseLootTable(name == null ? "Custom LootTable" : name, entries, rolls);
        }
    }


    @Builder
    @AllArgsConstructor
    public static class BaseLootTableItem implements LootTableItem {
        @Getter private final ItemStack item;
        @Getter private final int weigh;
        @Getter @Setter private double chance;
    }
}
