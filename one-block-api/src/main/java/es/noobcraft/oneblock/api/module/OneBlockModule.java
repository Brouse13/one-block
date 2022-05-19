package es.noobcraft.oneblock.api.module;

import lombok.Getter;

public class OneBlockModule {
    @Getter private String name;
    @Getter private OneBlockModuleSettings settings;

    public void load() {

    }

    public void unload() {

    }

    public void init(String name, OneBlockModuleSettings settings) {
        this.name = name;
        this.settings = settings;
    }
}
