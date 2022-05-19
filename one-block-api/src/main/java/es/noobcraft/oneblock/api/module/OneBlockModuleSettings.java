package es.noobcraft.oneblock.api.module;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;

public class OneBlockModuleSettings {
    private final YamlConfiguration configuration;

    public OneBlockModuleSettings(File file) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public OneBlockModuleSettings(InputStream stream) {
        this.configuration = YamlConfiguration.loadConfiguration(stream);
    }

    public String getAuthor() {
        return configuration.getString("author");
    }

    public int getVersion() {
        return configuration.getInt("version");
    }

    public String getMainClass() {
        return configuration.getString("main");
    }
}
