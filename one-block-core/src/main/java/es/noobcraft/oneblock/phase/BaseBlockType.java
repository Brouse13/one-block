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
    @Getter private final double probability;

    @Override
    public void spawn(World world) {
        Block block = OneBlockAPI.getSettings().getIslandSpawn().toLocation(world).getBlock();
        block.getState().setType(type.getType());
        block.getState().setData(type.getData());
        block.getState().update();
    }
}
