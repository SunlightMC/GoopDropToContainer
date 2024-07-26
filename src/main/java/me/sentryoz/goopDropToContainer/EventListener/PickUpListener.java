package me.sentryoz.goopDropToContainer.EventListener;

import gunging.ootilities.gunging_ootilities_plugin.compatibilities.GooPMMOItems;
import gunging.ootilities.gunging_ootilities_plugin.containers.GOOPCPersonal;
import gunging.ootilities.gunging_ootilities_plugin.containers.GOOPCTemplate;
import gunging.ootilities.gunging_ootilities_plugin.containers.loader.GCL_Templates;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import io.lumine.mythic.lib.api.item.NBTItem;

import static me.sentryoz.goopDropToContainer.GoopDropToContainer.plugin;

public class PickUpListener implements Listener {
    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();
        NBTItem itemNBT = NBTItem.get(itemStack);

        // Check if item is mmo items
        if (plugin.mmoItemsEnabled && itemNBT.hasType()) {
            String itemId = itemNBT.getString("MMOITEMS_ITEM_ID");
            String itemType = itemNBT.getString("MMOITEMS_ITEM_TYPE");
        }

        // Check if item is mythic mob item


        // Find container base on item

        GOOPCTemplate containerTemplate = GCL_Templates.getByInternalName("test_container");
        if (containerTemplate == null) {
            plugin.getLogger().warning("Could not find container for item " + item.getName());
            return;
        } else if (!containerTemplate.isPersonal()) {
            plugin.getLogger().warning("Container for item " + item.getName() + " is not personal");
            return;
        }
        GOOPCPersonal containerTemplateDeployed = (GOOPCPersonal) containerTemplate.getDeployed();

    }
}
