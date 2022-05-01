package es.noobcraft.oneblock.phase;

import es.noobcraft.oneblock.api.phases.SpecialActions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
public class BaseSpecialActions implements SpecialActions {
    @Getter private int block;
    @Getter private Actions action;
    @Getter private String value;
}
