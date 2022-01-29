package es.noobcraft.oneblock.phase;

import es.noobcraft.oneblock.api.phases.LootTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Builder
@AllArgsConstructor @NoArgsConstructor
public class BaseLootTable implements LootTable {
    @Getter private ItemStack item;
    @Getter private double probability;
    @Getter private int maxAmount;
}
