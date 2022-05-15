package es.noobcraft.oneblock.commands;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.utils.Items;
import es.noobcraft.oneblock.utils.Loaders;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

public class LobbyCommand implements PlayerCommand {
    private final Translator translator = Core.getTranslator();

    @Getter private final String[] aliases = new String[]{"lobby"};
    @Getter private final Set<Group> groups = Sets.newHashSet(Group.values());

    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        OneBlockPlayer oneBlockPlayer = OneBlockAPI.getPlayerCache().getPlayer(player.getName());

        //Unload the Current profile
        if (oneBlockPlayer.getCurrentProfile() != null)
            Loaders.unloadPlayer(oneBlockPlayer, oneBlockPlayer.getCurrentProfile());

        oneBlockPlayer.getBukkitPlayer().teleport(OneBlockAPI.getSettings().getLobbySpawn());

        player.getInventory().setItem(0, SpigotCore.getImmutableItemManager().makeImmutable(Items.getLobbyItem(oneBlockPlayer)));
        Logger.player(player, "one-block-messages.lobby.return");
    }
}
