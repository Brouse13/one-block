package es.noobcraft.oneblock.loaders;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.profile.ProfileManager;
import es.noobcraft.oneblock.logger.Logger;
import es.noobcraft.oneblock.logger.LoggerType;
import org.bukkit.Bukkit;
import org.bukkit.World;

public final class IslandLoad {
    private static final ProfileManager profileManager = OneBlockAPI.getProfileManager();

    public static void loadIsland(OneBlockPlayer player, OneBlockProfile profile) {
        //Check if the player is still on the profile
        if (profileManager.getProfile(player.getProfiles(), profile.getProfileName()) == null) return;

        //Check if world is loaded
        if(Bukkit.getWorlds().stream().map(World::getName).noneMatch(profile.getProfileName()::equals))
            OneBlockAPI.getWorldManager().loadWorld(profile.getProfileName(), false);
    }

    public static void unloadIsland(OneBlockProfile profile) {
        World world = Bukkit.getWorld(profile.getProfileName());

        if (world == null)return;

        if (world.getPlayers().size() == 0) {
            OneBlockAPI.getWorldManager().unloadWorld(world.getName());
            Logger.log(LoggerType.CONSOLE, "World unloaded successfully "+ world.getName());
        }
    }
}
