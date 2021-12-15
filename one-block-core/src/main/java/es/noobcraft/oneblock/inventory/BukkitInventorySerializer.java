package es.noobcraft.oneblock.inventory;

import es.noobcraft.oneblock.api.inventory.InventorySerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BukkitInventorySerializer implements InventorySerializer {
    @Override
    public byte[] serialize(ItemStack[] content) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            //Write the size of the inventory
            dataOutput.writeInt(content.length);

            //Save every element in the list
            for (ItemStack itemStack : content)
                dataOutput.writeObject(itemStack);

            //Serialize that array
            dataOutput.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    @Override
    public ItemStack[] deserialize(byte[] bytes) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            int size = dataInput.readInt();
            ItemStack[] content = new ItemStack[size];

            //  Read the serialized data
            for (int i = 0; i < content.length; i++)
                content[i] = ((ItemStack) dataInput.readObject());

            dataInput.close();
            return content;
        } catch (ClassNotFoundException | IOException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
