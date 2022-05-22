package es.noobcraft.oneblock.phase;

import es.noobcraft.oneblock.api.phases.LootTableItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Builder
@AllArgsConstructor
public class BaseLootTableItem implements LootTableItem {
    @Getter private ItemStack item;
    @Getter private int weigh;
}
