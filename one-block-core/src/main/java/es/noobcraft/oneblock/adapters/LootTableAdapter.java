package es.noobcraft.oneblock.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.oneblock.api.phases.LootTable;
import es.noobcraft.oneblock.phase.BaseLootTable;
import org.bukkit.Material;

import java.io.IOException;

public class LootTableAdapter extends TypeAdapter<LootTable> {

    @Override
    public LootTable read(JsonReader reader) throws IOException {
        final BaseLootTable.BaseLootTableBuilder lootTableBuilder = BaseLootTable.builder();
        reader.beginObject();
        String fieldName = null;

        while (reader.hasNext()) {
            if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

            if ("item".equals(fieldName)) {
                final String[] item = reader.nextString().split(":");

                ItemBuilder itemBuilder = ItemBuilder.from(Material.valueOf(item[0]));
                if (item.length > 1) itemBuilder.damage(Integer.parseInt(item[1]));

                lootTableBuilder.item(itemBuilder.build());
            }

            if("probability".equals(fieldName)) lootTableBuilder.probability(reader.nextDouble());

            if("maxAmount".equals(fieldName)) lootTableBuilder.maxAmount(reader.nextInt());

        }
        reader.endObject();
        return lootTableBuilder.build();
    }

    @Override
    public void write(JsonWriter writer, LootTable lootTable) throws IOException {
        writer.beginObject();
        writer.name("item").value(lootTable.getItem().getType().name()+ ":"+ lootTable.getItem().getDurability());
        writer.name("probability").value(lootTable.getProbability());
        writer.name("maxAmount").value(lootTable.getMaxAmount());
        writer.endObject();
    }
}
