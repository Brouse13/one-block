package es.noobcraft.oneblock.gui.flags;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.ClickableItem;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.permission.FlagEncoder;
import es.noobcraft.oneblock.api.permission.IslandFlag;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import java.util.BitSet;

public class FlagStatusGUI {
    private final Translator translator = Core.getTranslator();
    private final NoobInventory inventory;

    private final OneBlockPlayer player;
    private final String world;

    private final IslandFlag flag;
    private final String translatedFlag;

    public FlagStatusGUI(String world, IslandFlag flag, OneBlockPlayer player) {
        this.player = player;
        this.world = world;
        this.flag = flag;
        this.translatedFlag = translator.getLegacyText(player.getNoobPlayer(), "one-block.island.flags."+ flag.name().toLowerCase());

        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.flag-status.title", translatedFlag))
                .closeable(true)
                .size(9)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(update -> {}));
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());
        
        inventory.set(2, Items.getFlagStatusMembers(player, translatedFlag), getItemEvent(true));
        inventory.set(5, Items.getFlagStatusInvites(player, translatedFlag), getItemEvent(false));
        inventory.set(8, Items.getCloseInventory(player), event -> event.getWhoClicked().closeInventory());
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player.getNoobPlayer(), inventory);
    }

    private ClickableItem getItemEvent(boolean status) {
        return (event) -> {
            //Get the permission from the island and update the position
            BitSet permission = FlagEncoder.decode(OneBlockAPI.getPermissionManager().getPermission(world));
            permission.set(flag.getIndex(), status);

            //Update perms and notify player
            OneBlockAPI.getPermissionManager().updatePermission(world, FlagEncoder.encode(permission));
            Bukkit.getPlayer(player.getName()).closeInventory();
            Logger.player(player.getNoobPlayer(), "one-block.island.permissions-update");
        };
    }
}
