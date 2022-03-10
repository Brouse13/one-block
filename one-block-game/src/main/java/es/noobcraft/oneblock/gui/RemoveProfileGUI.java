package es.noobcraft.oneblock.gui;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class RemoveProfileGUI {
    private final Translator translator = Core.getTranslator();
    private final NoobInventory inventory;
    private final NoobPlayer noobPlayer;
    private final OneBlockPlayer oneBlockPlayer;

    public RemoveProfileGUI(NoobPlayer noobPlayer, OneBlockPlayer oneBlockPlayer) {
        this.noobPlayer = noobPlayer;
        this.oneBlockPlayer = oneBlockPlayer;
        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(noobPlayer, "one-block.inventory.remove-profiles.title", noobPlayer.getUsername()))
                .closeable(true)
                .rows(oneBlockPlayer.getProfiles().size() > 9 ? 4 : 3)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update));
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());

        for (int i = 0; i < oneBlockPlayer.getProfiles().size(); i++) {
            OneBlockProfile[] oneBlockProfiles = oneBlockPlayer.getProfiles().toArray(new OneBlockProfile[0]);
            OneBlockProfile profile = oneBlockProfiles[i];

            inventory.set(10+ i*2, ItemBuilder.from(oneBlockProfiles[i].getProfileItem())
                    .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.remove-profiles.profile.name", profile.getProfileName()))
                    .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.remove-profiles.profile.lore")).build(),
                    event -> {
                        event.getWhoClicked().closeInventory();
                        OneBlockAPI.getProfileLoader().deleteProfile(profile);
                        oneBlockPlayer.removeProfile(profile);
                        Logger.player(noobPlayer, "one-block.messages.profile-remove");
                    });
        }
    }

    private void update(NoobInventory inventory) {

    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(noobPlayer, inventory);
    }
}
