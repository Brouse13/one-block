package es.noobcraft.oneblock.player;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.player.OfflineOneBlockPlayer;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.player.PlayerSupplier;


public class BasePlayerSupplier implements PlayerSupplier {

    public OneBlockPlayer createPlayer(String username) {
        return new BaseOneBlockPlayer(username, OneBlockAPI.getSettings().getDefaultProfiles());
    }

    public OfflineOneBlockPlayer createOfflinePlayer(String username) {
        return new BaseOfflineOneBlockPlayer(username, OneBlockAPI.getSettings().getDefaultProfiles());
    }
}
