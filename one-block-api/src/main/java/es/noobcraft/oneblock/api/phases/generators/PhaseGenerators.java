package es.noobcraft.oneblock.api.phases.generators;

import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.phases.LootTable;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public final class PhaseGenerators {
    private static final Translator translator = Core.getTranslator();

    public static Generate generateTitle(OneBlockPlayer player, String value) {
        final String[] values = value.split(":");
        if (values.length > 1)
            return () -> player.getNoobPlayer().sendTitle(translator.getLegacyText(player.getNoobPlayer(), values[0]),
                    translator.getLegacyText(player.getNoobPlayer(), values[1]));
        return () -> player.getNoobPlayer().sendTitle(translator.getLegacyText(player.getNoobPlayer(), values[0]), "");
    }

    public static Generate generateActionBar(OneBlockPlayer player, String value) {
        return () -> player.getNoobPlayer().sendActionBar(new TextComponent(translator.getLegacyText(player.getNoobPlayer(), value)));
    }

    public static Generate generateMessage(OneBlockPlayer player, String message) {
        return () -> Logger.player(player.getNoobPlayer(), message);
    }

    public static Generate generateBlock(Block block, List<ItemStack> itemStacks) {
        return () -> {
            if (itemStacks.size() != 0)
                block.setType(itemStacks.get(((int) (Math.random() * itemStacks.size()))).getType());
            else
                block.setType(Material.GRASS);
            block.getState().update(true, true);
        };
    }

    public static Generate generateEntity(Block block, List<EntityType> entities) {
        return () -> block.getWorld().spawnEntity(block.getLocation().add(0, 1, 0), entities.get(((int) (Math.random() * entities.size()))));
    }

    public static Generate generateLootTable(Block block, List<List<LootTable>> lootTables) {
        //Generate a new chest on the location
        generateBlock(block, Collections.singletonList(new ItemStack(Material.CHEST))).generate();

        Chest chest = ((Chest) block.getState());
        //Foreach lootTable that it's get random, add the item to the chest
        return () -> lootTables.get(((int) (Math.random() * lootTables.size()))).forEach(lootTable -> {
            chest.getBlockInventory().addItem(new ItemStack(lootTable.getItem().getType(),
                    ((int)(Math.random() * lootTable.getMaxAmount())) + 1));
        });
    }
}
