package es.noobcraft.oneblock.loaders;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import es.noobcraft.core.api.Core;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.loaders.PhaseLoader;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.phases.Phase;
import es.noobcraft.oneblock.api.phases.PhaseBlocks;
import es.noobcraft.oneblock.phase.BasePhase;
import es.noobcraft.oneblock.phase.BasePhaseBlocks;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class JSONPhaseLoader implements PhaseLoader {
    private final Map<String, Phase> phases = Maps.newHashMap();
    private static final Map<String, PhaseBlocks> blocks = Maps.newHashMap();

    @Override
    public void loadPhases() {
        final File directory = new File(Core.getServerConfigurationsDirectory() + "/phases");
        if (!directory.mkdirs()) {
            final File[] json = directory.listFiles(file -> file.getName().endsWith(".json") && !file.isDirectory());

            if (json == null) {
                Logger.log(LoggerType.CONSOLE, "No phases loaded");
                return;
            }

            for (File file : json) {
                try (FileReader reader = new FileReader(file)) {
                    final BasePhase phase = OneBlockAPI.getGson().fromJson(reader, BasePhase.class);
                    this.phases.put(phase.getIdentifier(), phase);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Logger.log(LoggerType.CONSOLE, "Loaded "+ this.phases.size()+ " phases");
    }

    @Override
    public Set<Phase> getPhases() {
        return ImmutableSet.copyOf(phases.values());
    }

    @Override
    public PhaseBlocks getPhaseBlocks(String world) {
        if (!blocks.containsKey(world))
            blocks.put(world, new BasePhaseBlocks(world));
       return blocks.get(world);
    }

    public static void scheduleUpdate(JavaPlugin plugin) {
        Logger.log(LoggerType.CONSOLE, "Enabling blocks sync");
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,
                () -> blocks.values().forEach(PhaseBlocks::syncBlocks), 0L, 20L * 60L);
    }
}
