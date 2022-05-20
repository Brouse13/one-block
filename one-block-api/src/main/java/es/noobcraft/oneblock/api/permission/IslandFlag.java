package es.noobcraft.oneblock.api.permission;

import lombok.Getter;
import org.bukkit.Material;

public enum IslandFlag {
    PVP(Material.DIAMOND_SWORD, 0),
    SHEAR(Material.SHEARS, 1),
    BUILD(Material.GRASS, 2),
    BREAK(Material.DIAMOND_PICKAXE, 3),
    BREAD(Material.WHEAT, 4),
    DAMAGE(Material.ARROW, 5),
    LAND_ITEMS(Material.CARPET, 6),
    INVENTORIES(Material.CHEST, 7);

    @Getter private final Material material;
    @Getter private final int index;

    IslandFlag(Material material, int index) {
        this.material = material;
        this.index = index;
    }
}
