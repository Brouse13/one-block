package es.noobcraft.oneblock.profile;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.junit.runners.model.InitializationError;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public class BaseOneBlockProfile implements OneBlockProfile {
    @Getter private final OneBlockPlayer owner;
    @Getter private final String profileName;
    @Getter private final String islandOwner;
    @Getter private final byte[] inventory;
    @Getter private final int islandPermissions;
    @Getter private final Material profileItem;

    public BaseOneBlockProfile(OneBlockPlayer owner, String islandOwner, String profileName) {
        this.owner = owner;
        this.profileName = profileName;
        this.islandOwner = islandOwner;
        this.inventory = null;
        this.islandPermissions = 0;
        this.profileItem = OneBlockConstants.DEF_PROFILE_MATERIAL;
    }

    public BaseOneBlockProfile(OneBlockPlayer owner, String profileName) {
        this(owner, owner.getName(), profileName);
    }

    @SneakyThrows
    public BaseOneBlockProfile(ResultSet resultSet) {
        try {
            final Blob inventory = resultSet.getBlob("inventory");
            this.owner = OneBlockAPI.getPlayerCache().getPlayer(resultSet.getString("username"));
            this.profileName = resultSet.getString("name");
            this.islandOwner = resultSet.getString("island_owner");
            this.inventory = inventory == null ? null : inventory.getBytes(0, (int) inventory.length());
            this.islandPermissions = resultSet.getInt("island_permissions");
            this.profileItem = Material.valueOf(resultSet.getString("itemstack"));
        }catch (SQLException exception) {
            throw new InitializationError("Error reading profile");
        }
    }
}
