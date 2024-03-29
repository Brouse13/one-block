package es.noobcraft.oneblock.api.module;

import java.io.File;
import java.util.Set;

public interface ModuleLoader {

    /**
     * Get a OneBlockModule from the Module file
     * @param file module file
     * @return the OneBlock module
     */
    OneBlockModule getModule(File file);

    /**
     * Load all the modules on the directory
     * @apiNote It will attempt to load all .jar files
     * @param directory modules directory
     * @return All the modules on the directory
     */
    Set<OneBlockModule> getModules(File directory);

    /**
     * Get the Settings from the module, if it doesn't exist
     * it will return null
     * @param file Module as a file
     * @return the Module Settings
     */
    OneBlockModuleSettings getSettings(File file);

    /**
     * Get the Module as a file from a given name
     * @param moduleName the name of the module
     * @return the module as a File
     */
    File getFile(String moduleName);
}
