package es.noobcraft.oneblock.gui.flags;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.ClickableItem;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.flags.FlagEncoder;
import es.noobcraft.oneblock.api.flags.IslandFlag;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import java.util.Set;

public class FlagStatusGUI {
    private final Translator translator = Core.getTranslator();
    private final NoobInventory inventory;
    private final NoobPlayer noobPlayer;

    private final OneBlockPlayer oneBlockPlayer;
    private final Set<OneBlockProfile> profiles;

    private final IslandFlag flag;
    private final String transatedFlag;

    public FlagStatusGUI(Set<OneBlockProfile> profiles, IslandFlag flag, OneBlockPlayer oneBlockPlayer) {
        this.noobPlayer = Core.getPlayerCache().getPlayer(oneBlockPlayer.getName());
        this.oneBlockPlayer = oneBlockPlayer;
        this.profiles = profiles;
        this.flag = flag;
        this.transatedFlag = translator.getLegacyText(noobPlayer, "one-block.island.flags. "+ flag.name().toLowerCase());

        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(noobPlayer, "one-block.inventory.flag-status.title", transatedFlag))
                .closeable(true)
                .size(9)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update));
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++) inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());
        
        inventory.set(6, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(14)
                        .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.flag-status.owner.name"))
                        .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.flag-status.owner.lore", transatedFlag)).build(),
                getItemEvent(true));

        inventory.set(2, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(4)
                        .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.flag-status.coop.name"))
                        .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.flag-status.coop.lore", transatedFlag)).build(),
                event -> Logger.player(noobPlayer, "one-block.messages.not-implemented"));


        inventory.set(6, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(5)
                        .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.flag-status.all.name"))
                        .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.flag-status.all.lore", transatedFlag)).build(),
                getItemEvent(false));

        inventory.set(8, ItemBuilder.from(Material.ARROW)
                        .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.flag-status.close.name"))
                        .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.flag-status.close.lore")).build(),
                event -> event.getWhoClicked().closeInventory());
    }

    private void update(NoobInventory inventory) {}

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(noobPlayer, inventory);
    }

    private ClickableItem getItemEvent(boolean perms) {
        return (event) -> {
            profiles.forEach(profile -> {
                //Check if the profile is loaded on cache to update it
                OneBlockProfile cacheProfile = OneBlockAPI.getProfileCache().getProfile(oneBlockPlayer, profile.getProfileName());

                //Set the profile that will be updated
                OneBlockProfile updatableProfile = cacheProfile == null ? profile : cacheProfile;

                //Decode the perms and update the flag value
                //TODO modify perms modifying
                /*
                char[] decode = FlagEncoder.decode(updatableProfile.getIslandPermissions());
                decode[flag.getPos()] = perms ? '1' : '0';

                //Notice changes to database and cache
                updatableProfile.setIslandPermissions(FlagEncoder.encode(decode));
                 */
                OneBlockAPI.getProfileLoader().updateProfile(updatableProfile);
                Bukkit.getPlayer(oneBlockPlayer.getName()).closeInventory();
                Logger.player(noobPlayer, "one-block.island.permissions-update");
            });
        };
    }
}
