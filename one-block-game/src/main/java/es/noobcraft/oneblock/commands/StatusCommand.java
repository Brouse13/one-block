package es.noobcraft.oneblock.commands;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.permission.OneBlockGroups;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

public class StatusCommand implements PlayerCommand {
    private final Translator translator = Core.getTranslator();

    @Getter private final String[] aliases = new String[]{"status"};
    @Getter private final Set<Group> groups = Sets.newHashSet(OneBlockGroups.ADMIN);

    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append(translator.getLegacyText(player, "one-block.messages.status.title")).append("\n");

        //Loop throw all the loaded servers on the cache, if some server has no properties, has fall down
        for (String server : OneBlockAPI.getServerCache().getWorlds().keySet()) {
            if (Core.getServerRegistryManager().getProperties(server).isEmpty()) {
                builder.append(translator.getLegacyText(player, "one-block.messages.status.offline", server)).append("\n");
            }else {
                builder.append(translator.getLegacyText(player, "one-block.messages.status.online", server)).append("\n");
            }
        }
        player.sendMessage(builder.toString());
    }
}
