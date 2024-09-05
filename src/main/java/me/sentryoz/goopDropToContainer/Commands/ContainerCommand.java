package me.sentryoz.goopDropToContainer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

import net.Indyuce.mmoitems.api.Type;

import static me.sentryoz.goopDropToContainer.GoopDropToContainer.plugin;

import java.util.List;
import java.util.ArrayList;

public class ContainerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }


        if (command.getName().equalsIgnoreCase("addcontainer")) {
            if (args.length != 2) {
                player.sendMessage("Usage: /addcontainer <container_name> <items_type>");
                return true;
            }

            String containerName = args[0];
            String itemType = args[1].toUpperCase();

            // Check if itemType is a valid MMOItems type
            Type containerType = Type.get(itemType);
            if (containerType == null) {
                player.sendMessage("Invalid item type: " + itemType);
                return true;
            }


            // check if dropMaps section exists, create it if it doesn't
            ConfigurationSection dropMaps = plugin.getConfig().getConfigurationSection("DropMaps");
            if (dropMaps == null) {
                // create new section
                dropMaps = plugin.getConfig().createSection("DropMaps." + containerName);
            }

            List<String> containerList = dropMaps.getStringList(containerName);

            if (containerList == null) {
                containerList = new ArrayList<>();
            }

            // create a new container if it doesn't exist yet
            if (!dropMaps.contains(containerName)) {
                containerList.add(itemType);
                dropMaps.set(containerName, containerList);
                player.sendMessage("Container '" + containerName + "' with type '" + itemType + "' has been added.");
            } else if (containerList.contains(itemType)) {
                // check if container already contains the type
                player.sendMessage("Container '" + containerName + "' already contains type '" + itemType + "'.");
                return true;
            } else {
                containerList.add(itemType);
                dropMaps.set(containerName, containerList);
                player.sendMessage("Container '" + containerName + "' with type '" + itemType + "' has been added.");
            }

            plugin.saveConfig();
            return true;

        }

        return false;
    }
}
