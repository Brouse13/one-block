package es.noobcraft.oneblock.phase;

import es.noobcraft.oneblock.api.phases.LootTable;
import es.noobcraft.oneblock.api.phases.Phase;
import es.noobcraft.oneblock.api.phases.SpecialActions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@AllArgsConstructor
public class BasePhase implements Phase {
    @Getter private String identifier;
    @Getter private int minScore;
    @Getter private int maxScore;
    @Getter private List<ItemStack> items;
    @Getter private List<List<LootTable>> lootTables;
    @Getter private List<EntityType> entities;
    @Getter private Map<Integer, Set<SpecialActions>> specialActions;

}
