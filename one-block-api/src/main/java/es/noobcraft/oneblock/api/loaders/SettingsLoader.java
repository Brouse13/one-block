package es.noobcraft.oneblock.api.loaders;

import es.noobcraft.oneblock.api.settings.OneBlockSettings;

public interface SettingsLoader {
    /**
     * Load the default configuration from the database
     * @return the default configuration
     */
    OneBlockSettings loadSettings();
}
