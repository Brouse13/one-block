package es.noobcraft.oneblock.database;

import es.noobcraft.oneblock.api.database.ProfileLoader;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;

public class SqlProfileLoader implements ProfileLoader {
    @Override
    public boolean existProfile(OneBlockPlayer player, String name) {
        return false;
    }

    @Override
    public OneBlockProfile createProfile(OneBlockPlayer player, String name) {
        return null;
    }

    @Override
    public OneBlockProfile createCoop(OneBlockProfile profile, OneBlockPlayer target) {
        return null;
    }

    @Override
    public boolean deleteProfile(OneBlockPlayer player, String name) {
        return false;
    }

    @Override
    public boolean deleteCoop(OneBlockProfile profile, OneBlockPlayer target) {
        return false;
    }

    @Override
    public boolean updateProfile(OneBlockProfile profile) {
        return false;
    }
}
