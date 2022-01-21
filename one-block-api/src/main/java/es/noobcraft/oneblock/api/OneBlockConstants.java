package es.noobcraft.oneblock.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public final class OneBlockConstants {
    /**
     * Default player MAX_PROFILES
     */
    public static final int DEF_PROFILES = 3;

    /**
     * Default Material for new profiles created
     */
    public static final Material DEF_PROFILE_MATERIAL = Material.GRASS;

    /**
     * Default island permission to all the coops
     */
    public static int DEF_ISLAND_PERMISSION = 0x00;

    /**
     * Texture applied to ADD_PROFILE skull
     */
    public static String ADD_PROFILE_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19";

    /**
     * Texture applied to REMOVE_PROFILE skull
     */
    public static String REMOVE_PROFILE_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=";

    /**
     * Get the default spawn of the OneBlock lobby
     */
    public static Location SPAWN = new Location(Bukkit.getWorld("lobby"), 2, 62, 0, -90, 0);
}
