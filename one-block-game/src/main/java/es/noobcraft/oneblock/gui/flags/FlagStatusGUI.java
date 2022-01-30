package es.noobcraft.oneblock.gui.flags;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.ClickableItem;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.exceptions.FlagException;
import es.noobcraft.oneblock.api.flags.FlagEncoder;
import es.noobcraft.oneblock.api.flags.FlagType;
import es.noobcraft.oneblock.api.flags.IslandFlag;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class FlagStatusGUI {
    private final Translator translator = Core.getTranslator();
    private final NoobInventory inventory;
    private final NoobPlayer noobPlayer;

    private final OneBlockPlayer oneBlockPlayer;
    private final String world;

    private final IslandFlag flag;
    private final String translatedFlag;

    public FlagStatusGUI(String world, IslandFlag flag, OneBlockPlayer oneBlockPlayer) {
        this.noobPlayer = Core.getPlayerCache().getPlayer(oneBlockPlayer.getName());
        this.oneBlockPlayer = oneBlockPlayer;
        this.world = world;
        this.flag = flag;
        this.translatedFlag = translator.getLegacyText(noobPlayer, "one-block.island.flags."+ flag.name().toLowerCase());

        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(noobPlayer, "one-block.inventory.flag-status.title", translatedFlag))
                .closeable(true)
                .size(9)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update));
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++) inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());
        
        inventory.set(2, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(5)
                        .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.flag-status.owner.name"))
                        .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.flag-status.owner.lore", translatedFlag)).build(),
                getItemEvent(FlagType.OWNER));

        inventory.set(4, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(4)
                        .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.flag-status.coop.name"))
                        .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.flag-status.coop.lore", translatedFlag)).build(),
                getItemEvent(FlagType.COOP));


        inventory.set(6, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(3)
                        .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.flag-status.all.name"))
                        .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.flag-status.all.lore", translatedFlag)).build(),
                getItemEvent(FlagType.ALL));

        inventory.set(8, ItemBuilder.from(Material.ARROW)
                        .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.flag-status.close.name"))
                        .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.flag-status.close.lore")).build(),
                event -> event.getWhoClicked().closeInventory());
    }

    private void update(NoobInventory inventory) {}

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(noobPlayer, inventory);
    }

    private ClickableItem getItemEvent(FlagType type) {
        return (event) -> {
            //Decode perms
            char[] perms = FlagEncoder.decode(OneBlockAPI.getIslandPermissionLoader().getPermission(world, noobPlayer.getUsername()));
            //Update char[] perms
            try {
                FlagEncoder.setType(flag, perms, type);
            } catch (FlagException e) {
                e.printStackTrace();
                Logger.player(noobPlayer, "one-block.island.error.flag-deserialize");
                return;
            }
            //Update perms and notify player
            OneBlockAPI.getIslandPermissionLoader().updatePermission(world, noobPlayer.getUsername() ,FlagEncoder.encode(perms));
            Bukkit.getPlayer(oneBlockPlayer.getName()).closeInventory();
            Logger.player(noobPlayer, "one-block.island.permissions-update");
        };
    }
}
