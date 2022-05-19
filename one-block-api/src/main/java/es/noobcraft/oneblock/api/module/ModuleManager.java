package es.noobcraft.oneblock.api.module;

import java.util.Map;

public interface ModuleManager {
    /**
     * Get all the OneBlockModules loaded.
     * @return loaded OneBlockModules loaded
     */
    Map<String, OneBlockModule> getModules();

    /**
     * Load a specific module from the /modules dir.
     * If there's already a module with the same name
     * it won't load.
     * @param module module to load
     * @return if module was loaded
     */
    boolean loadModule(OneBlockModule module);

    /**
     * Unload a specific module from the modules. If there's
     * no module with that name it won't perform any action
     * @param moduleName name of the module
     */
    void unloadModule(String moduleName);

    /**
     * Reload the specific module. It won't look
     * in the modules' dir another time
     * @param moduleName name of the module
     */
    void reloadModule(String moduleName);

    /**
     * Unload all the loaded modules
     */
    void unloadAll();
}
