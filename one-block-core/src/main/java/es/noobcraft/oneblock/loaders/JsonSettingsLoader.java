package es.noobcraft.oneblock.loaders;

import es.noobcraft.core.api.Core;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.loaders.SettingsLoader;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.settings.OneBlockConstants;
import es.noobcraft.oneblock.api.settings.OneBlockSettings;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsonSettingsLoader implements SettingsLoader {
    final File file = new File(Core.getServerConfigurationsDirectory() + "/settings.json");

    @Override
    public OneBlockSettings loadSettings() {
        file.getParentFile().mkdirs();

        try (FileReader reader = new FileReader(file)) {
            return OneBlockAPI.getGson().fromJson(reader, OneBlockConstants.class);
        } catch (IOException exception) {
            Logger.log(LoggerType.ERROR, exception.getMessage());
            return null;
        }
    }
}
