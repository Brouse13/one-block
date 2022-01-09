package es.noobcraft.oneblock.api.flags;

import lombok.Getter;
import org.bukkit.Material;

public enum IslandFlags {
    PVP(Material.DIAMOND_SWORD, 0),
    SHEAR(Material.SHEARS, 1),
    BUILD(Material.GRASS, 2),
    BREAK(Material.DIAMOND_PICKAXE, 3),
    BREAD(Material.WHEAT, 4);

    @Getter private final Material material;
    @Getter private final int pos;

    IslandFlags(Material material, int pos) {
        this.material = material;
        this.pos = pos;
    }

    static int allPerms() {
        StringBuilder perms = new StringBuilder();
        for (int i = 0; i < IslandFlags.values().length; i++) perms.append("1");
        return Integer.parseInt(perms.toString());
    }
}
