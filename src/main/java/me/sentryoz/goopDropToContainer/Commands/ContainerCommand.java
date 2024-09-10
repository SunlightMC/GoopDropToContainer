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

public class ContainerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (command.getName().equalsIgnoreCase("addcontainer")) {
            if (args.length < 2 || args.length > 3) {
                player.sendMessage("Usage: /addcontainer <container_name> <items_type> <priority>");
                return true;
            }

            String containerName = args[0];
            String itemType = args[1].toUpperCase();
            int priority = 1; // default priority

            // Check if itemType is a valid MMOItems type
            Type containerType = Type.get(itemType);
            if (containerType == null) {
                player.sendMessage("Invalid item type: " + itemType);
                return true;
            }

            // accept input if it is a number and greater than 0
            if (args.length == 3) {
                try {
                    priority = Integer.parseInt(args[2]);
                    if (priority < 1) {
                        player.sendMessage("Priority must be greater than 0. Using default priority of 1.");
                        priority = 1;
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage("Invalid priority: " + args[2] + ". Using default priority of 1.");
                    priority = 1;
                }
            }

            // check if dropMaps section exists, create it if it doesn't
            ConfigurationSection dropMaps = plugin.getConfig().getConfigurationSection("DropMaps");
            if (dropMaps == null) {
                dropMaps = plugin.getConfig().createSection("DropMaps." + containerName);
            }

            ConfigurationSection containerSection = plugin.getConfig().getConfigurationSection("DropMaps." + containerName);
            if (containerSection == null) {
                // create new section under dropMaps
                containerSection = dropMaps.createSection(containerName);
            }

            // check if itemType already exists in container
            List<String> typeList = containerSection.getStringList("types");
            if (typeList.contains(itemType)) {
                player.sendMessage("Item type '" + itemType + "' already exists in container '" + containerName + "'.");
                return true;
            }

            typeList.add(itemType);
            containerSection.set("priority", priority);
            containerSection.set("types", typeList);

            player.sendMessage("Item type '" + itemType + "' added to container '" + containerName + "' with priority " + priority + ".");

            plugin.saveConfig();

            return true;

        }

        return false;
    }

}
