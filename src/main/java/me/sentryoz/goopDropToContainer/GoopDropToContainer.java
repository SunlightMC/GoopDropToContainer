package me.sentryoz.goopDropToContainer;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GoopDropToContainer extends JavaPlugin {

    public static GoopDropToContainer plugin;
    public boolean mmoItemsEnabled = false;
    public boolean mythicMobsEnabeld = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        checkDependenciesPlugin();


    }

    private void checkDependenciesPlugin() {
        // Check if have GOOP for container
        Plugin goopPlugin = getServer().getPluginManager().getPlugin("Gunging_Ootilities_Plugin");
        if (goopPlugin == null) {
            plugin.getLogger().warning("Goop plugin not found. Disabling...");
            this.setEnabled(false);
        }
        boolean haveItemManagerPlugin = false;

        // Check if have MMO Items
        Plugin mmoitems = getServer().getPluginManager().getPlugin("MMOItems");
        if (mmoitems != null) {
            haveItemManagerPlugin = true;
            plugin.getLogger().info("MMOItems plugin found.");
        } else {
            mmoItemsEnabled = true;
            plugin.getLogger().info("MMOItems plugin not found.");
        }
        // Check if have MythicMobs
        Plugin mythicmobs = getServer().getPluginManager().getPlugin("MythicMobs");
        if (mythicmobs != null) {
            haveItemManagerPlugin = true;
            mythicMobsEnabeld = true;
            plugin.getLogger().info("MythicMobs plugin found.");
        } else {
            plugin.getLogger().info("MythicMobs plugin not found.");
        }

        if (!haveItemManagerPlugin) {
            plugin.getLogger().warning("Not found any item manager plugin. Disabling...");
            this.setEnabled(false);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
