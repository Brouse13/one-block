package es.noobcraft.oneblock.api.module;

import java.io.File;

public interface ModuleLoader {

    /**
     * Get a OneBlockModule from the Module file
     * @param file module file
     * @return the OneBlock module
     */
    OneBlockModule getModule(File file);

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
