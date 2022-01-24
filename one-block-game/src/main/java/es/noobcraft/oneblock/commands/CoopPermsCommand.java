package es.noobcraft.oneblock.commands;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.gui.flags.FlagsGUI;
import es.noobcraft.oneblock.api.logger.Logger;
import lombok.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CoopPermsCommand implements PlayerCommand {
    @Override
    public @NonNull String[] getAliases() {
        return new String[]{"flags", "banderas"};
    }

    @Override
    public @NonNull Set<Group> getGroups() {
        return new HashSet<>(Arrays.asList(Group.values()));
    }

    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        final OneBlockPlayer oneBlockPlayer = OneBlockAPI.getPlayerCache().getPlayer(player.getUsername());

        OneBlockProfile profile = OneBlockAPI.getProfileManager().getProfile(oneBlockPlayer.getProfiles(), player.getWorld().getName());
        if (profile == null) {
            Logger.player(Core.getPlayerCache().getPlayer(oneBlockPlayer.getName()), "one-block.island.no-profile-found");
            return;
        }

        new FlagsGUI(oneBlockPlayer, profile).openInventory();
    }
}
