package es.noobcraft.oneblock.gui;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.item.SkullBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.profile.ProfileName;
import es.noobcraft.oneblock.loaders.PlayerLoader;
import es.noobcraft.oneblock.profile.BaseOneBlockProfile;
import es.noobcraft.oneblock.scoreboard.IslandScoreBoard;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.PlayerInventory;

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
                .updater(noobInventory -> {}));
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());
        if (player.getProfiles().size() == 0)
            inventory.set(10, ItemBuilder.from(Material.BARRIER)
                    .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.profiles.no-profiles.name"))
                    .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.profiles.no-profiles.lore")).build());
        else {
            int i = 0;
            for (OneBlockProfile profile : player.getProfiles()) {
                inventory.set(10+ i*2, ItemBuilder.from(profile.getProfileItem())
                        .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.profiles.profile-info.name", profile.getProfileName()))
                        .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.profiles.profile-info.lore")).build(),
                        event -> {
                            OneBlockAPI.getWorldLoader().loadWorld(profile.getWorldName(), false);
                            PlayerLoader.loadPlayer(player, profile);
                            player.setCurrentProfile(profile);
                            OneBlockAPI.getScoreboardManager().createScoreboard(player, new IslandScoreBoard(profile));
                        });
                i++;
            }
        }
        inventory.set(7, SkullBuilder.create().textures(OneBlockAPI.getSettings().getAddProfileTexture())
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.profiles.add-profile.name"))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.profiles.add-profile.lore")).build(),
                event -> {
                    if (player.getMaxProfiles() <= player.getProfiles().size())
                        Logger.player(player.getNoobPlayer(), "one-block.messages.max-profiles");
                    else
                        createProfile();
                    event.getWhoClicked().closeInventory();
                });

        inventory.set(8, SkullBuilder.create().textures(OneBlockAPI.getSettings().getRemoveProfileTexture())
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.profiles.remove-profile.name"))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.profiles.remove-profile.lore")).build(),
                event -> new RemoveProfileGUI(player.getNoobPlayer(), player).openInventory());

        inventory.set(35, ItemBuilder.from(Material.ARROW)
                        .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.profiles.close.name"))
                        .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.profiles.close.lore")).build(),
                event -> event.getWhoClicked().getOpenInventory().close());
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player.getNoobPlayer(), inventory);
    }

    private void createProfile() {
        //Create a new empty profile and add it to database
        OneBlockProfile profile = new BaseOneBlockProfile(player, player.getName(),
                String.valueOf(System.currentTimeMillis()), ProfileName.randomName(player.getProfiles()));
        OneBlockAPI.getProfileLoader().createProfile(profile);

        //Create the world and load the world blocks
        OneBlockAPI.getWorldLoader().createWorld(profile.getWorldName());
        OneBlockAPI.getPhaseLoader().getPhaseBlocks(profile.getWorldName());

        //Add the profile to the player and currentPlaying profile
        player.addProfile(profile);
        player.setCurrentProfile(profile);

        //Clear the player inventory and teleport to profile
        PlayerInventory inventory = profile.getOwner().getBukkitPlayer().getInventory();
        inventory.setItem(0, SpigotCore.getImmutableItemManager().makeMutable(inventory.getItem(0)));
        inventory.clear();
        player.teleport(profile);

        Logger.player(player.getNoobPlayer(), "one-block.messages.profile-created");

        Logger.player(player.getNoobPlayer(), "Bienvenido a OneBlock");
        Logger.player(player.getNoobPlayer(), "Rompe el bloque de abajo tuyo");
    }
}
