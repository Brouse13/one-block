package es.noobcraft.oneblock.phase;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.phases.BlockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

@Builder
@AllArgsConstructor
public class BaseBlockType implements BlockType {
    @Getter private final ItemStack type;
    @Getter private final int weigh;

    @Override
    @SuppressWarnings("deprecation")
    public void spawn(World world) {
        Block block = world.getBlockAt(OneBlockAPI.getSettings().getIslandSpawn().toLocation(world));
        block.setType(type.getType());
        block.setData(((byte) type.getDurability()));
        block.getState().update();
    }
}
