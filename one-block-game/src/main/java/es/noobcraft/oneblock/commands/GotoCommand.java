package es.noobcraft.oneblock.commands;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.permission.OneBlockGroups;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.Set;

public class GotoCommand implements PlayerCommand {
    @Getter private final String[] aliases = new String[]{"islandTp", "isTp"};
    @Getter private final Set<Group> groups = Sets.newHashSet(OneBlockGroups.STAFF);

    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        if (args.length < 1) {
            Logger.player(player, "one-block.messages.isTp.usage");
            return;
        }

        //Check if the Optional isn't empty and the server startsWith the Server type (one-block)
        Optional<String> server = Core.getOnlineManager().getServerName(args[0]);

        if (server.isPresent()) {
            if (!server.get().startsWith(Core.getServerType())) {
                Logger.player(player, "one-block.messages.isTp.no-one-block");
                return;
            }

            //If player is online tp, if not, teleport to the server
            Logger.player(player, "one-block.messages.isTp.teleporting", player.getName());
            if (server.get().equals(Core.getServerId()))
                Bukkit.getPlayer(player.getName()).teleport(Bukkit.getPlayer(args[0]));
            else
                player.connect(server.get());
        }else {
            Logger.player(player, "one-block.messages.isTp.not-found");
        }
    }
}
