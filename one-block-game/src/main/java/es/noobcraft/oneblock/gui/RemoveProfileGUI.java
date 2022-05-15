package es.noobcraft.oneblock.gui;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.utils.Items;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class RemoveProfileGUI {
    private final Translator translator = Core.getTranslator();

    private final NoobInventory inventory;
    private final OneBlockPlayer player;

    public RemoveProfileGUI(OneBlockPlayer player) {
        this.player = player;
        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.remove-profiles.title", player.getName()))
                .closeable(true)
                .rows(player.getProfiles().size() > 9 ? 4 : 3)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(update -> {}));
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());

        for (int i = 0; i < player.getProfiles().size(); i++) {
            OneBlockProfile[] oneBlockProfiles = player.getProfiles().toArray(new OneBlockProfile[0]);
            OneBlockProfile profile = oneBlockProfiles[i];

            inventory.set(10 + (i * 2), Items.getRemoveProfileInfo(player, profile), event -> {
                event.getWhoClicked().closeInventory();
                OneBlockAPI.getProfileLoader().deleteProfile(profile);
                player.removeProfile(profile);
                Logger.player(player.getNoobPlayer(), "one-block.messages.profile-remove");
            });
        }
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player.getNoobPlayer(), inventory);
    }
}
