package es.noobcraft.oneblock.module;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.module.ModuleManager;
import es.noobcraft.oneblock.api.module.OneBlockModule;
import lombok.NonNull;

import java.util.Map;

public class MapModuleManager implements ModuleManager {
    private final Map<String, OneBlockModule> modules = Maps.newHashMap();

    @Override
    public Map<String, OneBlockModule> getModules() {
        return ImmutableMap.copyOf(modules);
    }

    @Override
    public boolean loadModule(@NonNull OneBlockModule module) {
        if (modules.containsKey(module.getName())) {
            Logger.log(LoggerType.ERROR, "Unable to load module "+ module.getName()+ " already present");
            return false;
        }

        module.load();
        modules.put(module.getName(), module);
        return false;
    }

    @Override
    public void reloadModule(String module) {
        OneBlockModule oneBlockModule = modules.get(module);
        unloadModule(oneBlockModule.getName());
        loadModule(oneBlockModule);
    }

    @Override
    public void unloadModule(String name) {
        OneBlockModule module = modules.getOrDefault(name, null);
        if (module == null) {
            Logger.log(LoggerType.ERROR, "No module loaded with name "+ name);
            return;
        }

        module.unload();
        modules.remove(name);
    }

    @Override
    public void unloadAll() {
        modules.keySet().forEach(this::unloadModule);
    }
}
