package es.noobcraft.oneblock.adapters;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import es.noobcraft.oneblock.api.phases.*;
import es.noobcraft.oneblock.api.utils.WeighList;
import es.noobcraft.oneblock.phase.BasePhase;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PhaseAdapter extends TypeAdapter<Phase> {

    @Override
    public Phase read(JsonReader reader) throws IOException {
        final BasePhase.BasePhaseBuilder basePhaseBuilder = BasePhase.builder();
        reader.beginObject();
        String fieldName = null;

        //Loop throw main json obj
        while (reader.hasNext()) {
            if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

            if ("id".equals(fieldName)) basePhaseBuilder.identifier(reader.nextString());

            if ("min".equals(fieldName)) basePhaseBuilder.minScore(reader.nextInt());

            if ("max".equals(fieldName)) basePhaseBuilder.maxScore(reader.nextInt());

            if ("items".equals(fieldName)) {
                reader.beginArray();
                WeighList<BlockType> items = new WeighList<>();

                while (reader.hasNext())
                    items.add(new BlockTypeAdapter().read(reader));

                reader.endArray();
                basePhaseBuilder.items(items);
            }

            if ("mobs".equals(fieldName)) {
                reader.beginArray();
                WeighList<MobType> entities = new WeighList<>();

                while (reader.hasNext())
                    entities.add(new MobTypeAdapter().read(reader));

                reader.endArray();
                basePhaseBuilder.entities(entities);
            }

            if ("lootTable".equals(fieldName)) {
                List<LootTable> lootTables = Lists.newArrayList();
                reader.beginArray();

                //Loop throw all the possible lootTables array
                while (reader.hasNext())
                    lootTables.add(new LootTableAdapter().read(reader));

                reader.endArray();
                basePhaseBuilder.lootTables(lootTables);
            }

            if ("specialActions".equals(fieldName)) {
                reader.beginArray();
                Map<Integer, Set<SpecialActions>> actions = Maps.newHashMap();

                while (reader.hasNext()) {
                    SpecialActions action = new SpecialActionsAdapter().read(reader);

                    //If the block isn't on the map add it
                    if (!actions.containsKey(action.getBlock())) {
                        actions.put(action.getBlock(), Sets.newHashSet(action));
                        continue;
                    }
                    //If it's on it, add the block to the current Set
                    actions.put(action.getBlock(), Sets.union(actions.get(action.getBlock()), Sets.newHashSet(action)));
                }
                basePhaseBuilder.specialActions(actions);
                reader.endArray();
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
        for (BlockType entry : phase.getItems().getEntries())
            new BlockTypeAdapter().write(writer, entry);

        writer.endArray();

        writer.name("mobs").beginArray();
        for (MobType entry : phase.getEntities().getEntries())
            new MobTypeAdapter().write(writer, entry);
        writer.endArray();

        writer.name("lootTable").beginArray();
        for (LootTable lootTable : phase.getLootTables())
            new LootTableAdapter().write(writer, lootTable);
        writer.endArray();

        writer.name("specialActions").beginArray();
        for (Set<SpecialActions> actions : phase.getSpecialActions().values())
            for (SpecialActions action : actions)
                new SpecialActionsAdapter().write(writer, action);

        writer.endArray();
        writer.endObject();
    }
}
