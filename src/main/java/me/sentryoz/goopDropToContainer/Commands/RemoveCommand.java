package me.sentryoz.goopDropToContainer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

import static me.sentryoz.goopDropToContainer.GoopDropToContainer.plugin;

public class RemoveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            return true;
        }

        if (command.getName().equalsIgnoreCase("removecontainer")) {
            if (args.length < 1 || args.length > 2) {
                player.sendMessage("Usage: /removecontainer <container_name> [items_type]");
                return true;
            }

            String containerName = args[0];
            // check if itemType is provided
            String itemType = args.length == 2 ? args[1].toUpperCase() : null;

            // check if dropMaps section exists
            ConfigurationSection dropMaps = plugin.getConfig().getConfigurationSection("DropMaps");
            if (dropMaps == null) {
                player.sendMessage("No containers found.");
                return true;
            }

            if (!dropMaps.contains(containerName)) {
                player.sendMessage("Container '" + containerName + "' not found.");
                return true;
            }

            ConfigurationSection container = dropMaps.getConfigurationSection(containerName);
            if (itemType == null) {
                // remove entire container if itemType is not provided
                dropMaps.set(containerName, null);
                plugin.saveConfig();
                player.sendMessage("Container '" + containerName + "' has been removed.");
            } else {
                if (container != null) {
                    // remove itemType from container if itemType is provided
                    List < String > types = container.getStringList("types");
                    if (types.contains(itemType)) {
                        types.remove(itemType);
                        container.set("types", types);
                        plugin.saveConfig();
                        player.sendMessage("Item type '" + itemType + "' has been removed from container '" + containerName + "'.");
                    } else {
                        player.sendMessage("Item type '" + itemType + "' not found in container '" + containerName + "'.");
                    }
                } else {
                    player.sendMessage("Container '" + containerName + "' is empty or invalid.");
                }
            }

            return true;
        }

        return false;
        }
}
