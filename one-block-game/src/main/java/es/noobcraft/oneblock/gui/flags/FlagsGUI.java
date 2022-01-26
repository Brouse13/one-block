package es.noobcraft.oneblock.gui.flags;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.flags.IslandFlag;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class FlagsGUI {
    private final Translator translator = Core.getTranslator();

    private final NoobInventory inventory;
    private final NoobPlayer noobPlayer;
    private final OneBlockPlayer oneBlockPlayer;
    private final OneBlockProfile oneBlockProfile;

    public FlagsGUI(OneBlockPlayer oneBlockPlayer, OneBlockProfile profile) {
        this.noobPlayer = Core.getPlayerCache().getPlayer(oneBlockPlayer.getName());
        this.oneBlockPlayer = oneBlockPlayer;
        this.oneBlockProfile = profile;
        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(noobPlayer, "one-block.inventory.permissions.title", profile.getProfileName()))
                .closeable(true)
                .rows(2)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update));
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());

        int i = 0;
        for (IslandFlag flag : IslandFlag.values()) {
            String translatedFlag = translator.getLegacyText(noobPlayer, "one-block.island.flags."+ flag.name().toLowerCase());

            inventory.set(i, ItemBuilder.from(flag.getMaterial())
                    .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.permissions.flag.name", translatedFlag))
                    .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.permissions.flag.lore")).build(),
                        event -> new FlagStatusGUI(oneBlockProfile.getProfileName(), flag, oneBlockPlayer).openInventory());
            i++;
        }

        inventory.set(17, ItemBuilder.from(Material.ARROW)
                .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.permissions.close.name"))
                .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.permissions.close.lore")).build(),
                event -> Bukkit.getPlayer(oneBlockPlayer.getName()).closeInventory());
    }

    private void update(NoobInventory inventory) {}

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(noobPlayer, inventory);
    }
}
