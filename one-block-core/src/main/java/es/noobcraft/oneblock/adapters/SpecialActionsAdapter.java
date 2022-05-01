package es.noobcraft.oneblock.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import es.noobcraft.oneblock.api.phases.SpecialActions;
import es.noobcraft.oneblock.phase.BaseSpecialActions;

import java.io.IOException;

public class SpecialActionsAdapter extends TypeAdapter<SpecialActions> {
    @Override
    public SpecialActions read(JsonReader reader) throws IOException {
        final BaseSpecialActions.BaseSpecialActionsBuilder builder = BaseSpecialActions.builder();
        reader.beginObject();
        String fieldName = null;

        while (reader.hasNext()) {
            if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

            if ("block".equals(fieldName)) builder.block(reader.nextInt());
            if ("action".equals(fieldName)) builder.action(SpecialActions.Actions.valueOf(reader.nextString()));
            if ("value".equals(fieldName)) builder.value(reader.nextString());
        }
        reader.endObject();
        return builder.build();
    }

    @Override
    public void write(JsonWriter writer, SpecialActions action) throws IOException {
        writer.beginObject();
        writer.name("block").value(action.getBlock());
        writer.name("action").value(action.getAction().toString());
        writer.name("value").value(action.getValue());
        writer.endObject();
    }
}
