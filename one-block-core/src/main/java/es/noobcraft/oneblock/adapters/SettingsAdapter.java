package es.noobcraft.oneblock.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.settings.OneBlockConstants;
import es.noobcraft.oneblock.api.settings.OneBlockSettings;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.IOException;

public class SettingsAdapter extends TypeAdapter<OneBlockSettings> {
    @Override
    public OneBlockSettings read(JsonReader reader) throws IOException {
        OneBlockConstants.OneBlockConstantsBuilder builder = OneBlockConstants.builder();
        reader.beginObject();
        String fieldName = null;

        while (reader.hasNext()) {//Loop throw main json obj
            if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

            if ("defaultProfiles".equals(fieldName)) builder.defaultProfiles(reader.nextInt());
            if ("defaultMaterial".equals(fieldName)) builder.defaultMaterial(Material.valueOf(reader.nextString()));
            if ("defaultPermissions".equals(fieldName)) builder.defaultIslandPermission(reader.nextInt());
            if ("addProfileTexture".equals(fieldName)) builder.addProfileTexture(reader.nextString());
            if ("removeProfileTexture".equals(fieldName)) builder.removeProfileTexture(reader.nextString());
            if ("lobbySpawn".equals(fieldName)) builder.lobbySpawn(OneBlockAPI.getGson().fromJson(reader.nextString(), Location.class));
        }
        reader.endObject();
        return builder.build();
    }

    @Override
    public void write(JsonWriter writer, OneBlockSettings settings) throws IOException {
        writer.beginObject();
        writer.name("defaultProfiles").value(settings.getDefaultProfiles());
        writer.name("defaultMaterial").value(settings.getDefaultMaterial().toString());
        writer.name("defaultPermissions").value(settings.getDefaultIslandPermission());
        writer.name("addProfileTexture").value(settings.getRemoveProfileTexture());
        writer.name("removeProfileTexture").value(settings.getRemoveProfileTexture());
        writer.name("lobbySpawn");
        new LocationAdapter().write(writer, settings.getLobbySpawn());
        writer.endObject();
    }
}
