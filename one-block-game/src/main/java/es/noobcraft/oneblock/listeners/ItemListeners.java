package es.noobcraft.oneblock.listeners;

import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.gui.ProfileGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListeners implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final ItemStack itemInHand = event.getItem();
        if(itemInHand == null) return;

        if (SpigotCore.getNBTTagHelper().hasKey(itemInHand, "event")) {
            String value = SpigotCore.getNBTTagHelper().getValue(itemInHand, "event");
            if (value.equals("profile-list")) new ProfileGUI(OneBlockAPI.getPlayerCache().getPlayer(event.getPlayer().getName())).openInventory();
        }
    }
}
