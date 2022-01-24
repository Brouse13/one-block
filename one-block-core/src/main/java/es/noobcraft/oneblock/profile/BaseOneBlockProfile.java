package es.noobcraft.oneblock.profile;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.inventory.InventorySerializer;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class BaseOneBlockProfile implements OneBlockProfile {
    @Getter private OneBlockPlayer owner;
    @Getter private String profileName;
    @Getter private String islandOwner;
    @Getter @Setter private byte[] inventory;
    @Getter private Material profileItem;

    public BaseOneBlockProfile(OneBlockPlayer owner, String islandOwner, String profileName, int islandPermissions) {
        this.owner = owner;
        this.profileName = profileName;
        this.islandOwner = islandOwner;
        this.inventory = InventorySerializer.serialize(new ItemStack[0]);
        this.profileItem = OneBlockConstants.DEF_PROFILE_MATERIAL;
    }

    public BaseOneBlockProfile(ResultSet resultSet) {
        try {
            final Blob inventory = resultSet.getBlob("inventory");
            this.owner = OneBlockAPI.getPlayerCache().getPlayer(resultSet.getString("username"));
            this.profileName = resultSet.getString("name");
            this.islandOwner = resultSet.getString("island_owner");
            this.inventory = inventory == null ? null : inventory.getBytes(1, (int) inventory.length());
            this.profileItem = Material.valueOf(resultSet.getString("itemstack"));
        }catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
