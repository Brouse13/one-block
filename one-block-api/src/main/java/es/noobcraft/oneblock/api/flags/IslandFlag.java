package es.noobcraft.oneblock.api.flags;

import lombok.Getter;
import org.bukkit.Material;

public enum IslandFlag {
    PVP(Material.DIAMOND_SWORD, 0),
    SHEAR(Material.SHEARS, 1),
    BUILD(Material.GRASS, 2),
    BREAK(Material.DIAMOND_PICKAXE, 3),
    BREAD(Material.WHEAT, 4),
    DAMAGE(Material.ARROW, 5),
    LAND_ITEMS(Material.CARPET, 6);

    @Getter private final Material material;
    @Getter private final int pos;

    IslandFlag(Material material, int pos) {
        this.material = material;
        this.pos = pos;
    }
}
