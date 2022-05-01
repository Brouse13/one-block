package es.noobcraft.oneblock.adapters;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.oneblock.api.phases.LootTable;
import es.noobcraft.oneblock.api.phases.Phase;
import es.noobcraft.oneblock.api.phases.SpecialActions;
import es.noobcraft.oneblock.phase.BasePhase;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

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

        while (reader.hasNext()) {//Loop throw main json obj
            if (reader.peek().equals(JsonToken.NAME)) fieldName = reader.nextName();

            if ("id".equals(fieldName)) basePhaseBuilder.identifier(reader.nextString());

            if ("min".equals(fieldName)) basePhaseBuilder.minScore(reader.nextInt());

            if ("max".equals(fieldName)) basePhaseBuilder.maxScore(reader.nextInt());

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
                List<List<LootTable>> setLootTables = Lists.newArrayList();

                while (reader.hasNext()) {//Loop throw all the possible lootTables array
                    reader.beginArray();
                    List<LootTable> lootTables = Lists.newArrayList();

                    while (reader.hasNext()) //Loop throw lootTables array
                        lootTables.add(new LootTableAdapter().read(reader));
                    setLootTables.add(lootTables);
                    reader.endArray();
                }
                reader.endArray();
                basePhaseBuilder.lootTables(setLootTables);
            }

            if ("specialActions".equals(fieldName)) {
                reader.beginArray();
                Map<Integer, Set<SpecialActions>> actions = Maps.newHashMap();

                while (reader.hasNext()) {
                    //Create a new SpecialAction
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
        for (ItemStack itemStack : phase.getItems())
            writer.value(itemStack.getType().name()+ ":"+ itemStack.getDurability());
        writer.endArray();

        writer.name("mobs").beginArray();
        for (EntityType entity : phase.getEntities())
            writer.value(entity.name());
        writer.endArray();

        writer.name("lootTable").beginArray();
        for (List<LootTable> lootTables : phase.getLootTables()) {
            writer.beginArray();
            for (LootTable lootTable : lootTables) {
                writer.beginObject();
                writer.name("item").value(lootTable.getItem().getType().name()+ ":"+ lootTable.getItem().getDurability());
                writer.name("probability").value(lootTable.getProbability());
                writer.name("maxAmount").value(lootTable.getMaxAmount());
                writer.endObject();
            }
            writer.endArray();
        }
        writer.endArray();

        writer.name("specialActions").beginArray();
        for (Set<SpecialActions> actions : phase.getSpecialActions().values())
            for (SpecialActions action : actions)
                new SpecialActionsAdapter().write(writer, action);

        writer.endArray();
        writer.endObject();
    }

    private ItemStack itemLoader(String[] item) {
        ItemBuilder itemBuilder = ItemBuilder.from(Material.valueOf(item[0]));
        if (item.length > 1) itemBuilder.damage(Integer.parseInt(item[1]));

        return itemBuilder.build();
    }
}
