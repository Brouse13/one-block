package es.noobcraft.oneblock.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import es.noobcraft.oneblock.api.phases.MobType;
import es.noobcraft.oneblock.phase.BaseMobType;

import java.io.IOException;

public class MobTypeAdapter extends TypeAdapter<MobType> {

    @Override
    public void write(JsonWriter writer, MobType mobType) throws IOException {
        writer.beginObject();
        writer.name("entity").value(mobType.getEntity().name());
        if (mobType.getProbability() != -1)
            writer.name("probability").value(mobType.getProbability());
        writer.endObject();
    }

    @Override
    public MobType read(JsonReader reader) throws IOException {
        BaseMobType.BaseMobTypeBuilder builder = BaseMobType.builder();
        builder.probability(-1);
        reader.beginObject();
        String fieldName = null;

        while (reader.hasNext()) {//Loop throw main json obj
            if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

            if ("entity".equals(fieldName)) builder.entity(MobType.Type.valueOf(reader.nextString()));
            if ("probability".equals(fieldName)) builder.probability(reader.nextDouble());
        }
        reader.endObject();
        return builder.build();
    }
}
