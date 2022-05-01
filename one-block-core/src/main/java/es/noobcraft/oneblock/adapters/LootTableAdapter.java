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

/**
 * LootTableAdapter V2
 */
public class LootTableAdapter extends TypeAdapter<LootTable> {

    @Override
    public LootTable read(JsonReader reader) throws IOException {
        final BaseLootTable.BaseLootTableBuilder builder = BaseLootTable.builder();
        reader.beginObject();
        String fieldName = null;

        //Loop throw main json object
        while (reader.hasNext()) {
            if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

            if("name".equals(fieldName)) builder.name(reader.nextString());

            if ("rolls".equals(fieldName)) builder.rolls(reader.nextInt());

            if("items".equals(fieldName)) {
                reader.beginArray();

                //Loop throw all LootTableItems
                while (reader.hasNext()) {
                    BaseLootTable.BaseLootTableItem.BaseLootTableItemBuilder item = BaseLootTable.BaseLootTableItem.builder();
                    reader.beginObject();
                    if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

                    //Loop throw each LootTableItems elements
                    while (reader.hasNext()) {
                        if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

                        if ("item".equals(fieldName)) {
                            final String[] itemStr = reader.nextString().split(":");

                            ItemBuilder itemBuilder = ItemBuilder.from(Material.valueOf(itemStr[0]));
                            if (itemStr.length > 1) itemBuilder.damage(Integer.parseInt(itemStr[1]));

                            item.item(itemBuilder.build());
                        }

                        if("weigh".equals(fieldName)) item.weigh(reader.nextInt());
                    }
                    reader.endObject();
                    builder.addItem(item.build());
                }
                reader.endArray();
            }
        }
        reader.endObject();
        return builder.build();
    }

    @Override
    public void write(JsonWriter writer, LootTable lootTable) throws IOException {
        writer.beginObject();
        writer.name("name").value(lootTable.getName());
        writer.name("rolls").value(lootTable.getRolls());
        writer.name("items").beginArray();

        for (LootTable.LootTableItem item : lootTable.getItems()) {
            writer.beginObject();
            writer.name("item").value(item.getItem().getType().name()+ ":"+ item.getItem().getDurability());
            writer.name("weigh").value(item.getWeigh());
            writer.endObject();
        }
    }
}
