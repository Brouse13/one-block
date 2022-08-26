package es.noobcraft.oneblock.commands;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.profile.BaseOneBlockProfile;
import es.noobcraft.oneblock.utils.InviteManager;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

public class CoopInviteCommand implements PlayerCommand {
    @Getter private final String[] aliases = new String[]{"coopInvite"};
    @Getter private final Set<Group> groups = Sets.newHashSet(Group.values());

    @Override
    public void run(@NonNull BukkitNoobPlayer noobPlayer, @NonNull String label, @NonNull String[] args) {
        //Check command arguments
        if (args.length < 1) {
            Logger.player(noobPlayer, "one-block.messages.invite.usage");
            return;
        }

        //Check if the player is on a profile now if not refuse command
        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(noobPlayer.getName());
        OneBlockProfile currentProfile = player.getCurrentProfile();
        if (currentProfile == null) {
            Logger.player(noobPlayer, "one-block.messages.invite.not-in-island");
            return;
        }

        if (!player.getCurrentProfile().getOwner().getName()
                .equals(player.getCurrentProfile().getIslandOwner())) {
            Logger.player(noobPlayer, "one-block.messages.invite.not-owner");
            return;
        }

        //Check if target is online
        if (OneBlockAPI.getPlayerCache().getPlayer(args[0]) == null) {
            Logger.player(noobPlayer, "one-block.messages.invite.target-not-online", args[0]);
            return;
        }
        OneBlockPlayer target = OneBlockAPI.getPlayerCache().getPlayer(args[0]);

        //Check if the player isn't on the profile and has available
        if (target.getProfiles().stream()
                .filter(profile -> profile.getProfileName().equals(currentProfile.getProfileName()))
                .anyMatch(profile -> profile.getIslandOwner().equals(currentProfile.getIslandOwner()))) {
            Logger.player(noobPlayer, "one-block.messages.invite.already-on-profile");
            return;
        }

        //Check if player has available profiles
        if (target.getProfiles().size() >= 3) {
            Logger.player(noobPlayer, "one-block.messages.invite.max-profiles");
            return;
        }

        //Create the invitation
        InviteManager.addPlayer(target.getName(), new BaseOneBlockProfile(target, currentProfile.getIslandOwner(),
                currentProfile.getWorldName(), currentProfile.getProfileName()));
        Logger.player(noobPlayer, "one-block.messages.invite.invited");
        Logger.player(target.getNoobPlayer(), "one-block.messages.invite.target-invited", noobPlayer.getName());
    }
}
