package es.noobcraft.oneblock.commands;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.utils.InviteManager;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

public class CoopRemoveCommand implements PlayerCommand {
    @Getter private final String[] aliases = new String[]{"coopRemove"};
    @Getter private final Set<Group> groups = Sets.newHashSet(Group.values());

    @Override
    public void run(@NonNull BukkitNoobPlayer noobPlayer, @NonNull String label, @NonNull String[] args) {
        //Check command arguments
        if (args.length < 1) {
            Logger.player(noobPlayer, "one-block.messages.remove.usage");
            return;
        }

        //Check if the player is on a profile now if not refuse command
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(noobPlayer.getName());
        if (player.getCurrentProfile() == null) {
            Logger.player(noobPlayer, "one-block.messages.remove.not-in-island");
            return;
        }

        if (!player.getCurrentProfile().getOwner().getName()
                .equals(player.getCurrentProfile().getIslandOwner())) {
            Logger.player(noobPlayer, "one-block.messages.remove.not-owner");
            return;
        }

        OneBlockPlayer target = OneBlockAPI.getPlayerCache().getPlayer(args[0]);

        //Check if the player is on the profile
        if (target.getProfiles().stream().map(OneBlockProfile::getProfileName)
                .noneMatch(player.getCurrentProfile().getProfileName()::equals)) {
            Logger.player(noobPlayer, "one-block.messages.remove.not-in-profile");
            return;
        }

        //Cancel any pending invitation
        InviteManager.removePlayer(target.getName());

        //Get the profile from the cache and remove it on the database
        OneBlockProfile targetProfile = OneBlockAPI.getProfileCache().getProfile(target, player.getCurrentProfile().getProfileName());
        OneBlockAPI.getProfileCache().removeProfile(targetProfile);
        target.removeProfile(targetProfile);
        OneBlockAPI.getProfileLoader().deleteProfile(targetProfile);

        Logger.player(noobPlayer, "one-block.messages.remove.removed");
    }
}
