package es.noobcraft.oneblock.commands;

import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.gui.ProfileGUI;
import lombok.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProfileCommand implements PlayerCommand {
    @Override
    public @NonNull String[] getAliases() {
        return new String[]{"profiles", "perfiles"};
    }

    @Override
    public @NonNull Set<Group> getGroups() {
        return new HashSet<>(Arrays.asList(Group.values()));
    }

    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        final OneBlockPlayer oneBlockPlayer = OneBlockAPI.getPlayerCache().getPlayer(player.getUsername());

        new ProfileGUI(oneBlockPlayer).openInventory();
    }
}
