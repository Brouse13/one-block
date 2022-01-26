package es.noobcraft.oneblock.api.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public final class InventorySerializer {
    public static byte[] serialize(ItemStack[] content) {
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

    public static ItemStack[] deserialize(byte[] bytes) throws IOException {
        //Bytes from NULL
        byte[] nullBytes = new byte[]{0x4e, 0x55, 0x4c, 0x4c};

        if (Arrays.equals(bytes, nullBytes) || bytes == null) return new ItemStack[0];

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
