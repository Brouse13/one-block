package es.noobcraft.oneblock.api.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;


@Builder @AllArgsConstructor @NoArgsConstructor
public final class OneBlockConstants implements OneBlockSettings {
    @Getter private int defaultProfiles = 3;
    @Getter private Material defaultMaterial = Material.GRASS;
    @Getter private int defaultIslandPermission = 262143;
    @Getter private String addProfileTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19";
    @Getter private String removeProfileTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0";
    @Getter private Location lobbySpawn = new Location(Bukkit.getWorld("lobby"), 2, 62, 0, -90, 0);
    @Getter private Vector islandSpawn = new Vector(0, 50, 0);
}
