package es.noobcraft.oneblock.module;

import es.noobcraft.core.api.Core;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.module.ModuleLoader;
import es.noobcraft.oneblock.api.module.OneBlockModule;
import es.noobcraft.oneblock.api.module.OneBlockModuleSettings;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class BaseModuleLoader implements ModuleLoader {
    @Override
    public OneBlockModule getModule(File file) {
        //Load settings from the module `module.yml if there are null, return null`
        OneBlockModuleSettings settings = getSettings(file);

        if (settings == null) return null;

        //Attempt to load class from the module
        try {
            final URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, OneBlockModule.class.getClassLoader());

            try {
                final Class<?> loaded = loader.loadClass(settings.getMainClass());

                if (OneBlockModule.class.isAssignableFrom(loaded)) {
                    return loaded.asSubclass(OneBlockModule.class).newInstance();
                }else {
                    Logger.log(LoggerType.ERROR, "Class "+ loaded.getName()+ " doesn't extend from OneBlockModule");
                }
            } catch (ClassNotFoundException exception) {
                Logger.log(LoggerType.ERROR, "Unable to get class "+ settings.getMainClass());
            } catch (final NoClassDefFoundError ignored) {}
        }catch (IOException | InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public OneBlockModuleSettings getSettings(File file) {
        try {
            JarFile jarFile = new JarFile(file);

            JarEntry module = jarFile.getJarEntry("module.yml");

            if (module == null) {
                Logger.log(LoggerType.ERROR, "Module " + file.getName() + " don't have module.yml");
                return null;
            }

            OneBlockModuleSettings settings = new OneBlockModuleSettings(jarFile.getInputStream(module));

            if (settings.getMainClass() == null || settings.getAuthor() == null) {
                Logger.log(LoggerType.ERROR, "Malformed module.yml from " + file.getName());
                return null;
            }

            return settings;
        }catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public File getFile(String moduleName) {
        return new File(Core.getServerConfigurationsDirectory().getAbsolutePath() +
                "/modules/"+ moduleName+ ".jar");
    }
}
