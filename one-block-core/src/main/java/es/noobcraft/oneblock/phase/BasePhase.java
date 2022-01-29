package es.noobcraft.oneblock.phase;

import es.noobcraft.oneblock.api.phases.LootTable;
import es.noobcraft.oneblock.api.phases.Phase;
import lombok.*;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Builder
@AllArgsConstructor @NoArgsConstructor
public class BasePhase implements Phase {
    @Getter private String identifier;
    @Getter private int minScore;
    @Getter private int maxScore;
    @Getter private List<ItemStack> items;
    @Getter private List<LootTable> lootTables;
    @Getter private List<EntityType> entities;
}
