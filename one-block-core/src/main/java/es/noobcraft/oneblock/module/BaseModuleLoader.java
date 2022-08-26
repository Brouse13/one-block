package es.noobcraft.oneblock.module;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.Core;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.module.ModuleLoader;
import es.noobcraft.oneblock.api.module.OneBlockModule;
import es.noobcraft.oneblock.api.module.OneBlockModuleSettings;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class BaseModuleLoader implements ModuleLoader {
    private final JavaPlugin plugin;

    public BaseModuleLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

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
                    final OneBlockModule oneBlockModule = loaded.asSubclass(OneBlockModule.class).newInstance();
                    oneBlockModule.init(plugin, file.getName().split(".jar")[0], settings);
                    return oneBlockModule;
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
    public Set<OneBlockModule> getModules(File directory) {
        final File[] files = directory.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null) return Sets.newHashSet();
        return Arrays.stream(files).map(this::getModule).collect(Collectors.toSet());
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
