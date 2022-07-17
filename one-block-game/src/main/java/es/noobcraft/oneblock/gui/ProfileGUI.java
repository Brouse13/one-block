package es.noobcraft.oneblock.gui;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import java.util.Set;

public class ProfileGUI {
    private final Translator translator = Core.getTranslator();
    private final NoobInventory inventory;
    private final NoobPlayer noobPlayer;
    private final Set<OneBlockProfile> profiles;

    public ProfileGUI(NoobPlayer noobPlayer, Set<OneBlockProfile> profiles) {
        this.noobPlayer = noobPlayer;
        this.profiles = profiles;
        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(noobPlayer, "one-block.inventory.profiles.title", noobPlayer.getUsername()))
                .closeable(true)
                .rows(4)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update)
        );
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());
        if (profiles.size() == 0)
            inventory.set(10, ItemBuilder.from(Material.BARRIER)
                    .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.profiles.no-profiles.name"))
                    .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.profiles.no-profiles.lore")).build());
        else {
            for (int i = 0; i < profiles.size(); i++) {
                OneBlockProfile[] oneBlockProfiles = profiles.toArray(new OneBlockProfile[0]);
                inventory.set(10+ i*2, ItemBuilder.from(oneBlockProfiles[i].getProfileItem())
                        .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.profiles.profile-info.name", oneBlockProfiles[i].getProfileName()))
                        .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.profiles.profile-info.name")).build());
            }
        }
        inventory.set(35, ItemBuilder.from(Material.ARROW)
                .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.profiles.close.name"))
                .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.profiles.close.lore")).build(),
                event -> event.getWhoClicked().getOpenInventory().close());
    }

    private void update(NoobInventory inventory) {

    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(noobPlayer, inventory);
    }
}
