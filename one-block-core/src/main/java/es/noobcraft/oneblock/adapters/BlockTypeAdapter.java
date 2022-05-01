package es.noobcraft.oneblock.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.oneblock.api.phases.BlockType;
import es.noobcraft.oneblock.phase.BaseBlockType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class BlockTypeAdapter extends TypeAdapter<BlockType> {
    @Override
    public BlockType read(JsonReader reader) throws IOException {
        BaseBlockType.BaseBlockTypeBuilder builder = BaseBlockType.builder();
        builder.probability(-1);
        reader.beginObject();
        String fieldName = null;

        while (reader.hasNext()) {//Loop throw main json obj
            if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

            if ("type".equals(fieldName)) builder.type(itemLoader(reader.nextString().split(":")));

            if ("probability".equals(fieldName)) builder.probability(reader.nextDouble());
        }
        reader.endObject();
        return builder.build();
    }

    @Override
    public void write(JsonWriter writer, BlockType blockType) throws IOException {
        writer.beginObject();
        writer.name("type").value(blockType.getType().getType().name()+ ":"+ blockType.getType().getDurability());
        if (blockType.getProbability() != -1)
            writer.name("probability").value(blockType.getProbability());
        writer.endObject();
    }

    private ItemStack itemLoader(String[] item) {
        ItemBuilder itemBuilder = ItemBuilder.from(Material.valueOf(item[0]));
        if (item.length > 1) itemBuilder.damage(Integer.parseInt(item[1]));

        return itemBuilder.build();
    }
}
