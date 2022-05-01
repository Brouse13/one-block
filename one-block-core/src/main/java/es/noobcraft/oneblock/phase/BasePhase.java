package es.noobcraft.oneblock.phase;

import es.noobcraft.oneblock.api.phases.*;
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
    @Getter private List<BlockType> items;
    @Getter private List<LootTable> lootTables;
    @Getter private List<MobType> entities;
    @Getter private Map<Integer, Set<SpecialActions>> specialActions;

}
