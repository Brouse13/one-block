package es.noobcraft.oneblock.api.phases;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import net.md_5.bungee.api.chat.TextComponent;

public final class PhaseGenerators {
    private static final Translator translator = Core.getTranslator();

    public static void generateTitle(OneBlockPlayer player, String value) {
        final String[] values = value.split(":");
        if (values.length > 1)
            player.getNoobPlayer().sendTitle(translator.getLegacyText(player.getNoobPlayer(), values[0]),
                    translator.getLegacyText(player.getNoobPlayer(), values[1]));
        player.getNoobPlayer().sendTitle(translator.getLegacyText(player.getNoobPlayer(), values[0]), "");
    }

    public static void generateActionBar(OneBlockPlayer player, String value) {
        player.getNoobPlayer().sendActionBar(new TextComponent(translator.getLegacyText(player.getNoobPlayer(), value)));
    }

    public static void generateMessage(OneBlockPlayer player, String message) {
        Logger.player(player.getNoobPlayer(), message);
    }
}
