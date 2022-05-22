package es.noobcraft.oneblock.api.phases;

import es.noobcraft.oneblock.api.utils.Weigh;
import org.bukkit.inventory.ItemStack;

public interface LootTableItem extends Weigh {

    ItemStack getItem();
}
