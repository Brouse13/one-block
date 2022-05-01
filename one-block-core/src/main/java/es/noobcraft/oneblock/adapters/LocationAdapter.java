package es.noobcraft.oneblock.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.IOException;

public class LocationAdapter extends TypeAdapter<Location> {

    @Override
    public Location read(JsonReader reader) throws IOException {
        Location location = new Location(null, 0, 0, 0, 0, 0);
        reader.beginObject();
        String fieldName = null;

        while (reader.hasNext()) {//Loop throw main json obj
            if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

            if ("world".equals(fieldName)) location.setWorld(Bukkit.getWorld(reader.nextString()));
            if ("x".equals(fieldName)) location.setX(reader.nextDouble());
            if ("y".equals(fieldName)) location.setY(reader.nextDouble());
            if ("z".equals(fieldName)) location.setZ(reader.nextDouble());
            if ("yaw".equals(fieldName)) location.setYaw(reader.nextInt());
            if ("pitch".equals(fieldName)) location.setPitch(reader.nextInt());
        }
        reader.endObject();
        return location;
    }

    @Override
    public void write(JsonWriter writer, Location location) throws IOException {
        writer.beginObject();
        writer.name("x").value(location.getX());
        writer.name("y").value(location.getY());
        writer.name("z").value(location.getZ());
        writer.name("yaw").value(location.getYaw());
        writer.name("pitch").value(location.getPitch());
        writer.endObject();
    }
}
