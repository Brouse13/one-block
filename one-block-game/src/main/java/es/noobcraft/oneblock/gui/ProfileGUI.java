package es.noobcraft.oneblock.gui;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.item.SkullBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.loaders.IslandLoad;
import es.noobcraft.oneblock.loaders.PlayerLoader;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.scoreboard.IslandScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.util.Vector;

public class ProfileGUI {
    private final Translator translator = Core.getTranslator();
    private final NoobInventory inventory;
    private final NoobPlayer noobPlayer;
    private final OneBlockPlayer oneBlockPlayer;

    public ProfileGUI(OneBlockPlayer oneBlockPlayer) {
        this.noobPlayer = Core.getPlayerCache().getPlayer(oneBlockPlayer.getName());
        this.oneBlockPlayer = oneBlockPlayer;
        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(noobPlayer, "one-block.inventory.profiles.title", noobPlayer.getUsername()))
                .closeable(true)
                .rows(oneBlockPlayer.getProfiles().size() > 9 ? 4 : 3)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update));
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());
        if (oneBlockPlayer.getProfiles().size() == 0)
            inventory.set(10, ItemBuilder.from(Material.BARRIER)
                    .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.profiles.no-profiles.name"))
                    .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.profiles.no-profiles.lore")).build());
        else {
            int i = 0;
            for (OneBlockProfile profile : oneBlockPlayer.getProfiles()) {
                inventory.set(10+ i*2, ItemBuilder.from(profile.getProfileItem())
                        .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.profiles.profile-info.name", profile.getProfileName()))
                        .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.profiles.profile-info.lore")).build(),
                        event -> {
                            IslandLoad.loadIsland(oneBlockPlayer, profile);
                            PlayerLoader.loadPlayer(oneBlockPlayer, profile);
                            oneBlockPlayer.setCurrentProfile(profile);
                            OneBlockAPI.getScoreboardManager().createScoreboard(oneBlockPlayer, new IslandScoreBoard(profile));
                        });
                i++;
            }
        }
        inventory.set(7, SkullBuilder.create().textures(OneBlockConstants.ADD_PROFILE_TEXTURE)
                .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.profiles.add-profile.name"))
                .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.profiles.add-profile.lore")).build(),
                event -> {
                    if (oneBlockPlayer.getMaxProfiles() <= oneBlockPlayer.getProfiles().size()) {
                        event.getWhoClicked().closeInventory();
                        Logger.player(noobPlayer, "one-block.messages.max-profiles");
                        return;
                    }

                    event.getWhoClicked().closeInventory();
                    createProfile();
                });

        inventory.set(8, SkullBuilder.create().textures(OneBlockConstants.REMOVE_PROFILE_TEXTURE)
                .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.profiles.remove-profile.name"))
                .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.profiles.remove-profile.lore")).build(),
                event -> new RemoveProfileGUI(noobPlayer, oneBlockPlayer).openInventory());
        inventory.set(35, ItemBuilder.from(Material.ARROW)
                        .displayName(translator.getLegacyText(noobPlayer, "one-block.inventory.profiles.close.name"))
                        .lore(translator.getLegacyTextList(noobPlayer, "one-block.inventory.profiles.close.lore")).build(),
                event -> event.getWhoClicked().getOpenInventory().close());
    }

    private void update(NoobInventory inventory) {}

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(noobPlayer, inventory);
    }

    private void createProfile() {
        final String worldName = System.currentTimeMillis() + "";
        final OneBlockProfile profile = OneBlockAPI.getProfileLoader().createProfile(oneBlockPlayer, oneBlockPlayer, -1, worldName);
        oneBlockPlayer.addProfile(profile);
        oneBlockPlayer.setCurrentProfile(profile);

        Logger.player(noobPlayer, "one-block.messages.profile-created");

        final World world = Bukkit.getWorld(worldName);
        final Location location = new Location(world, 0, 30, 0);

        world.getBlockAt(location).setType(Material.GRASS);
        Bukkit.getPlayer(noobPlayer.getUsername()).teleport(location.add(new Vector(0, 1, 0)));
    }
}
