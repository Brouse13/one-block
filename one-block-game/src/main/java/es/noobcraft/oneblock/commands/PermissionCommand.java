package es.noobcraft.oneblock.commands;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.gui.flags.FlagsGUI;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PermissionCommand implements PlayerCommand {
    @Getter private final String[] aliases = new String[]{"flags", "permisos"};
    @Getter private final Set<Group> groups = Sets.newHashSet(Group.values());

    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        OneBlockPlayer oneBlockPlayer = OneBlockAPI.getPlayerCache().getPlayer(player.getUsername());

        //Check if player is on a profile to perform the command
        if (oneBlockPlayer.getCurrentProfile() == null) {
            Logger.player(Core.getPlayerCache().getPlayer(oneBlockPlayer.getName()), "one-block.island.no-profile-found");
            return;
        }

        //Check if the player is the owner of the profile
        if (!oneBlockPlayer.getCurrentProfile().getIslandOwner().equals(player.getName())) {
            Logger.player(Core.getPlayerCache().getPlayer(oneBlockPlayer.getName()), "one-block.island.no-owner");
            return;
        }

        new FlagsGUI(oneBlockPlayer, oneBlockPlayer.getCurrentProfile()).openInventory();
    }
}
