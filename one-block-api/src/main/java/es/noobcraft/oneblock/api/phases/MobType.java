package es.noobcraft.oneblock.api.phases;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import lombok.Getter;
import org.bukkit.entity.*;

import java.util.function.BiConsumer;

public interface MobType extends Probability {
    /**
     * Get the OneBlockEntityType
     * @return the EntityType
     */
    Type getEntity();

    /**
     * Spawn the given entity into the world
     * @param player player who broke the block
     */
    void summon(OneBlockPlayer player);

    enum Type {
        BLAZE(Blaze.class),
        CAT(Ocelot.class, (ocelot, player) -> {
            ocelot.setCatType(Ocelot.Type.SIAMESE_CAT);
            ocelot.setTamed(true);
            ocelot.setSitting(true);
            ocelot.setOwner(player.getBukkitPlayer());
        }),
        CAVE_SPIDER(CaveSpider.class),
        CHICKEN(Chicken.class),
        COW(Cow.class),
        CREEPER(Creeper.class),
        DONKEY(Horse.class, (horse, player) -> {
            horse.setVariant(Horse.Variant.DONKEY);
            horse.setTamed(true);
        }),
        ELDER_GUARDIAN(Guardian.class, (guardian, player) -> guardian.setElder(true)),
        ENDERMAN(Enderman.class),
        ENDERMITE(Endermite.class),
        FISH(FishHook.class),
        GHAST(Ghast.class),
        GUARDIAN(Guardian.class),
        HORSE(Horse.class),
        MAGMA_CUBE(MagmaCube.class),
        MUSHROOM_COW(MushroomCow.class),
        MULE(Horse.class, (horse, player) -> {
            horse.setVariant(Horse.Variant.MULE);
            horse.setTamed(true);
        }),
        OCELOT(Ocelot.class),
        PIG(Pig.class),
        PIG_ZOMBIE(PigZombie.class),
        RABBIT(Rabbit.class),
        SILVERFISH(Silverfish.class),
        SHEEP(Sheep.class),
        SPIDER(Spider.class),
        SKELETON(Skeleton.class),
        SLIME(Slime.class),
        SQUID(Squid.class),
        WITCH(Witch.class),
        VILLAGER(Villager.class),
        WITHER_SKELETON(Skeleton.class, (skeleton, player) -> skeleton.setSkeletonType(Skeleton.SkeletonType.WITHER)),
        WOLF(Wolf.class),
        ZOMBIE(Zombie.class);

        @Getter private final Class aClass;
        @Getter private final BiConsumer consumer;

        <T> Type(Class<T> aClass, BiConsumer<T, OneBlockPlayer> consumer) {
            this.aClass = aClass;
            this.consumer = consumer;
        }

        <T> Type(Class<T> aClass) {
            this.aClass = aClass;
            this.consumer = (T, K) -> {};
        }
    }
}
