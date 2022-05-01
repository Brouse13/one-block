package es.noobcraft.oneblock.phase;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.phases.MobType;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

@Builder
@AllArgsConstructor
public class BaseMobType implements MobType {
    @Getter private final Type entity;
    @Getter private final double probability;

    @Override
    public void summon(OneBlockPlayer player) {
        World world = player.getBukkitPlayer().getWorld();
        Entity spawn = world.spawn(
                OneBlockAPI.getSettings().getIslandSpawn().toLocation(world).add(new Vector(0, 1, 0)),
                entity.getAClass());
        entity.getConsumer().accept(spawn, player);
    }
}
