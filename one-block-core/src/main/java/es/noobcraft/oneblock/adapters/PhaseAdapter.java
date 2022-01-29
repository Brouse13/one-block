package es.noobcraft.oneblock.adapters;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.oneblock.api.phases.LootTable;
import es.noobcraft.oneblock.api.phases.Phase;
import es.noobcraft.oneblock.phase.BasePhase;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;

public class PhaseAdapter extends TypeAdapter<Phase> {

    @Override
    public Phase read(JsonReader reader) throws IOException {
        final BasePhase.BasePhaseBuilder basePhaseBuilder = BasePhase.builder();
        reader.beginObject();
        String fieldName = null;

        while (reader.hasNext()) {//Loop throw main json obj
            if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

            if ("id".equals(fieldName)) basePhaseBuilder.identifier(reader.nextString());

            if ("min".equals(fieldName)) basePhaseBuilder.minScore(reader.nextInt());

            if ("max".equals(fieldName)) basePhaseBuilder.minScore(reader.nextInt());

            if ("items".equals(fieldName)) {
                reader.beginArray();
                List<ItemStack> items = Lists.newArrayList();

                while (reader.hasNext()) items.add(itemLoader(reader.nextString().split(":")));

                reader.endArray();
                basePhaseBuilder.items(items);
            }

            if ("mobs".equals(fieldName)) {
                reader.beginArray();
                List<EntityType> entities = Lists.newArrayList();

                while (reader.hasNext()) entities.add(EntityType.valueOf(reader.nextString()));

                reader.endArray();
                basePhaseBuilder.entities(entities);
            }

            if ("lootTable".equals(fieldName)) {
                reader.beginArray();
                List<LootTable> lootTables = Lists.newArrayList();

                while (reader.hasNext()) lootTables.add(new LootTableAdapter().read(reader));

                reader.endArray();
                basePhaseBuilder.lootTables(lootTables);
            }
        }
        reader.endObject();
        return basePhaseBuilder.build();
    }

    @Override
    public void write(JsonWriter writer, Phase phase) throws IOException {
        writer.beginObject();
        writer.name("id").value(phase.getIdentifier());
        writer.name("min").value(phase.getMinScore());
        writer.name("max").value(phase.getMaxScore());

        writer.name("items").beginArray();
        for (ItemStack itemStack : phase.getItems())
            writer.value(itemStack.getType().name()+ ":"+ itemStack.getDurability());
        writer.endArray();

        writer.name("mobs").beginArray();
        for (EntityType entity : phase.getEntities())
            writer.value(entity.name());
        writer.endArray();

        writer.name("lootTable").beginArray();
        for (LootTable lootTable : phase.getLootTables()) {
            writer.beginObject();
            writer.name("item").value(lootTable.getItem().getType().name()+
                    ":"+ lootTable.getItem().getDurability());
            writer.name("probability").value(lootTable.getProbability());
            writer.name("maxAmount").value(lootTable.getMaxAmount());
            writer.endObject();
        }
        writer.endArray();
        writer.endObject();
    }

    private ItemStack itemLoader(String[] item) {
        ItemBuilder itemBuilder = ItemBuilder.from(Material.valueOf(item[0]));
        if (item.length > 1) itemBuilder.damage(Integer.parseInt(item[1]));

        return itemBuilder.build();
    }
}
