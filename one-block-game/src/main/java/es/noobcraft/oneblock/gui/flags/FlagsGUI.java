package es.noobcraft.oneblock.gui.flags;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.oneblock.api.permission.IslandFlag;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class FlagsGUI {
    private final Translator translator = Core.getTranslator();

    private final NoobInventory inventory;
    private final OneBlockPlayer player;
    private final OneBlockProfile profile;

    public FlagsGUI(OneBlockPlayer player, OneBlockProfile profile) {
        this.player = player;
        this.profile = profile;
        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.permissions.title", profile.getProfileName()))
                .closeable(true)
                .rows(2)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update));
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());

        for (IslandFlag flag : IslandFlag.values()) {
            String translatedFlag = translator.getLegacyText(player.getNoobPlayer(), "one-block.island.flags."+ flag.name().toLowerCase());

            inventory.add(Items.getFlags(player, flag, translatedFlag), event -> new FlagStatusGUI(profile.getProfileName(), flag, player).openInventory());
        }

        inventory.set(17, Items.getCloseInventory(player), event -> Bukkit.getPlayer(player.getName()).closeInventory());
    }

    private void update(NoobInventory inventory) {}

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player.getNoobPlayer(), inventory);
    }
}
