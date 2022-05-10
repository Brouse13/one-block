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

public class CoopAcceptCommand implements PlayerCommand {
    @Getter private final String[] aliases = new String[]{"coopAccept"};
    @Getter private final Set<Group> groups = Sets.newHashSet(Group.values());

    @Override
    public void run(@NonNull BukkitNoobPlayer noobPlayer, @NonNull String label, @NonNull String[] args) {
        if(!InviteManager.hasInvitations(noobPlayer.getName())) {
            Logger.player(noobPlayer, "one-block.messages.accept.no-invites");
            return;
        }

        OneBlockPlayer player = OneBlockAPI.getPlayerCache().getPlayer(noobPlayer.getName());
        OneBlockProfile profile = InviteManager.getProfile(noobPlayer.getName());

        if (player.getProfiles().size() >= 3) {
            return;
        }

        //Add the profile to the database and player
        OneBlockAPI.getProfileLoader().createProfile(profile);
        OneBlockAPI.getProfileCache().addProfile(profile);
        player.addProfile(profile);

        //Remove the profile from the invitations
        InviteManager.removePlayer(player.getName());

        Logger.player(noobPlayer, "one-block.messages.accept.accepted");
    }
}
