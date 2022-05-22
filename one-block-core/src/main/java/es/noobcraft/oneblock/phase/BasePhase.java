package es.noobcraft.oneblock.phase;

import es.noobcraft.oneblock.api.phases.*;
import es.noobcraft.oneblock.api.utils.WeighList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@AllArgsConstructor
public class BasePhase implements Phase {
    @Getter private String identifier;
    @Getter private int minScore;
    @Getter private int maxScore;
    @Getter private WeighList<BlockType> items;
    @Getter private WeighList<MobType> entities;
    @Getter private List<LootTable> lootTables;
    @Getter private Map<Integer, Set<SpecialActions>> specialActions;

}
