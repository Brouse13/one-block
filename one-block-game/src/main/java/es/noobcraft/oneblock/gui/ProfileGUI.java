package es.noobcraft.oneblock.gui;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.utils.Items;
import es.noobcraft.oneblock.utils.Loaders;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class ProfileGUI {
    private final Translator translator = Core.getTranslator();
    private final NoobInventory inventory;
    private final OneBlockPlayer player;

    public ProfileGUI(OneBlockPlayer oneBlockPlayer) {
        this.player = oneBlockPlayer;
        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.profiles.title", player.getName()))
                .closeable(true)
                .rows(oneBlockPlayer.getProfiles().size() > 9 ? 4 : 3)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(update -> {}));
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());

        if (player.getProfiles().size() == 0) {
            inventory.set(10, Items.getNoProfiles(player));
        }else {
            int i = 0;
            for (OneBlockProfile profile : player.getProfiles()) {
                inventory.set(10+ i*2, Items.getProfileInfo(player, profile), event -> Loaders.loadProfile(profile, player));
                i++;
            }
        }

        inventory.set(7, Items.getAddProfile(player), event -> {
            if (player.getMaxProfiles() <= player.getProfiles().size())
                Logger.player(player.getNoobPlayer(), "one-block.messages.max-profiles");
            else
                Loaders.createProfile(player);
            event.getWhoClicked().closeInventory();
        });

        inventory.set(8, Items.getRemoveProfile(player), event -> new RemoveProfileGUI(player).openInventory());

        inventory.set(26, Items.getCloseInventory(player), event -> event.getWhoClicked().getOpenInventory().close());
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player.getNoobPlayer(), inventory);
    }
}
