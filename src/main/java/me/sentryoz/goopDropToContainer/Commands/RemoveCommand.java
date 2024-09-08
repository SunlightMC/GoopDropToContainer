package me.sentryoz.goopDropToContainer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

import static me.sentryoz.goopDropToContainer.GoopDropToContainer.plugin;

public class RemoveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            return true;
        }

        if (command.getName().equalsIgnoreCase("removecontainer")) {
            if (args.length != 1) {
                player.sendMessage("Usage: /removecontainer <container_name>");
                return true;
            }

            String containerName = args[0];

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

            dropMaps.set(containerName, null);
            plugin.saveConfig();
            player.sendMessage("Container '" + containerName + "' has been removed.");
            return true;
        }

        return false;
    }
}
