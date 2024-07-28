package me.sentryoz.goopDropToContainer.EventListener;

import gunging.ootilities.gunging_ootilities_plugin.containers.GOOPCPersonal;
import gunging.ootilities.gunging_ootilities_plugin.containers.GOOPCTemplate;
import gunging.ootilities.gunging_ootilities_plugin.containers.inventory.ISLPersonalContainer;
import gunging.ootilities.gunging_ootilities_plugin.containers.inventory.ISSPersonalContainer;
import gunging.ootilities.gunging_ootilities_plugin.containers.loader.GCL_Templates;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import io.lumine.mythic.lib.api.item.NBTItem;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static me.sentryoz.goopDropToContainer.GoopDropToContainer.plugin;

public class PickUpListener implements Listener {
    final int maxStack = 64;

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();
        NBTItem itemNBT = NBTItem.get(itemStack);

        // Check if item is mmo items
        String itemType;
        String itemId;
        if (plugin.mmoItemsEnabled && itemNBT.hasType()) {
            itemType = itemNBT.getString("MMOITEMS_ITEM_TYPE");
            itemId = itemNBT.getString("MMOITEMS_ITEM_ID");
        } else {
            itemType = null;
            itemId = null;
        }
        if (plugin.debug) {
            plugin.getLogger().info("item type: " + itemType);
            plugin.getLogger().info("item id: " + itemId);
        }

        // Find container base on item
        ConfigurationSection dropMaps = plugin.getConfig().getConfigurationSection("DropMaps");
        if (dropMaps == null) {
            return;
        }
        boolean foundContainer = false;
        String containerName = null;
        for (String key : dropMaps.getKeys(false)) {
            List<String> list = (List<String>) plugin.getConfig().getList("DropMaps." + key);
            for (String configType : list) {
                if (Objects.equals(configType, itemType)) {
                    foundContainer = true;
                    containerName = key;
                    break;
                }
            }
            if (foundContainer) {
                break;
            }
        }

        if (plugin.debug) {
            plugin.getLogger().info("Container name: " + containerName);
        }

        if (!foundContainer) {
            return;
        }

        GOOPCTemplate containerTemplate = GCL_Templates.getByInternalName(containerName);
        if (containerTemplate == null) {
            plugin.getLogger().warning("Could not find container " + containerName);
            return;
        } else if (!containerTemplate.isPersonal()) {
            plugin.getLogger().warning("Container" + containerName + " is not personal");
            return;
        }

        GOOPCPersonal personalContainer = (GOOPCPersonal) containerTemplate.getDeployed();
        int size = containerTemplate.getTotalSlotCount();

        for (int slot = 0; slot < size; slot++) {
            if (plugin.debug) {
                plugin.getLogger().info("Checking slot " + slot);
            }

            if (containerTemplate.isStorageSlot(slot)) {
                if (plugin.debug) {
                    plugin.getLogger().info("Slot " + slot + " is storage slot");
                }
                // Temporary Item Stack for evaluation
                ISSPersonalContainer itemSlot = new ISSPersonalContainer(slot, null, personalContainer, null);
                itemSlot.setElaborator(player);
                ISLPersonalContainer location = itemSlot.getItem(player);

                // Get item from location
                ItemStack containerItem = location.getItem();
                NBTItem itemInvNBT = NBTItem.get(containerItem);
                String itemInvType = itemInvNBT.getString("MMOITEMS_ITEM_TYPE");
                String itemInvId = itemInvNBT.getString("MMOITEMS_ITEM_ID");

                // If slot don't have anything, set item to that slot
                if (containerItem == null || containerItem.getType() == Material.AIR) {
                    if (plugin.debug) {
                        plugin.getLogger().info("Slot " + slot + " is null/Air");
                    }
                    location.setItem(itemStack);
                    event.getItem().remove();
                    event.setCancelled(true);
                    break;
                }

                // Check if item is match with pick up item
                if (plugin.debug) {
                    plugin.getLogger().info("storage item stack: " + containerItem.getType());
                    plugin.getLogger().info("storage item type: " + itemInvType);
                    plugin.getLogger().info("storage item id: " + itemInvId);
                }
                if (Objects.equals(itemType, itemInvType) && Objects.equals(itemId, itemInvId)) {
                    // Check if that slot can hold all pickup item. If not, split it
                    int canHold = maxStack - containerItem.getAmount();
                    if (itemStack.getAmount() > canHold) {
                        containerItem.setAmount(maxStack);
                        itemStack.setAmount(itemStack.getAmount() - canHold);
                        location.setItem(containerItem);
                        event.getItem().setItemStack(itemStack);
                    } else {
                        containerItem.setAmount(containerItem.getAmount() + itemStack.getAmount());
                        event.getItem().remove();
                        event.setCancelled(true);
                        break;
                    }
                }
            } else {
                if (plugin.debug) {
                    plugin.getLogger().info("Slot " + slot + " is not storage slot");
                }
            }
        }
    }
}
