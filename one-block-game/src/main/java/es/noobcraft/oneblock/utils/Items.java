package es.noobcraft.oneblock.utils;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.item.SkullBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.permission.IslandFlag;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Items {
    private static final Translator translator = Core.getTranslator();

    public static ItemStack getLobbyItem(OneBlockPlayer player) {
        return SpigotCore.getImmutableItemManager().makeImmutable(ItemBuilder.from(Material.NETHER_STAR)
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.player.profile-list.name"))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.player.profile-list.lore"))
                .metadata("event", "profile-list").build());
    }

    public static ItemStack getAddProfile(OneBlockPlayer player) {
        return SkullBuilder.create().textures(OneBlockAPI.getSettings().getAddProfileTexture())
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.profiles.add-profile.name"))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.profiles.add-profile.lore")).build();
    }

    public static ItemStack getRemoveProfile(OneBlockPlayer player) {
        return SkullBuilder.create().textures(OneBlockAPI.getSettings().getRemoveProfileTexture())
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.profiles.remove-profile.name"))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.profiles.remove-profile.lore")).build();
    }

    public static ItemStack getFlagStatusInvites(OneBlockPlayer player, String translatedFlag) {
        return ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(4)
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.flag-status.owner.name"))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.flag-status.owner.lore", translatedFlag)).build();
    }

    public static ItemStack getFlagStatusMembers(OneBlockPlayer player, String translatedFlag) {
        return ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(5)
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.flag-status.coop.name"))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.flag-status.coop.lore", translatedFlag)).build();
    }

    public static ItemStack getProfileInfo(OneBlockPlayer player, OneBlockProfile profile) {
        return ItemBuilder.from(profile.getProfileItem())
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.profiles.profile-info.name", profile.getProfileName()))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.profiles.profile-info.lore")).build();
    }

    public static ItemStack getRemoveProfileInfo(OneBlockPlayer player, OneBlockProfile profile) {
        return ItemBuilder.from(profile.getProfileItem())
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.remove-profiles.profile.name", profile.getProfileName()))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.remove-profiles.profile.lore")).build();
    }

    public static ItemStack getNoProfiles(OneBlockPlayer player) {
        return ItemBuilder.from(Material.BARRIER)
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.profiles.no-profiles.name"))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.profiles.no-profiles.lore")).build();
    }

    public static ItemStack getFlags(OneBlockPlayer player, IslandFlag flag, String translatedFlag) {
        return ItemBuilder.from(flag.getMaterial())
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.permissions.flag.name", translatedFlag))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.permissions.flag.lore")).build();
    }

    public static ItemStack getCloseInventory(OneBlockPlayer player) {
        return ItemBuilder.from(Material.ARROW)
                .displayName(translator.getLegacyText(player.getNoobPlayer(), "one-block.inventory.close.name"))
                .lore(translator.getLegacyTextList(player.getNoobPlayer(), "one-block.inventory.close.lore")).build();
    }
}
