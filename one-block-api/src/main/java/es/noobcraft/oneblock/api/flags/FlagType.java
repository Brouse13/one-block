package es.noobcraft.oneblock.api.flags;

import lombok.Getter;

public enum FlagType {
    OWNER(0),
    COOP(1),
    FRIENDS(2),
    ALL(3);

    @Getter private final int value;

    FlagType(int value) {
        this.value = value;
    }
}
