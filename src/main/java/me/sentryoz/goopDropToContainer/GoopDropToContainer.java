package me.sentryoz.goopDropToContainer;

import me.sentryoz.goopDropToContainer.Commands.RemoveCommand;
import me.sentryoz.goopDropToContainer.EventListener.PickUpListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.sentryoz.goopDropToContainer.Commands.ContainerCommand;

public final class GoopDropToContainer extends JavaPlugin {

    public static GoopDropToContainer plugin;
    public boolean mmoItemsEnabled = false;
    public boolean debug = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        plugin.saveDefaultConfig();
        checkDebug();
        checkDependenciesPlugin();
        getServer().getPluginManager().registerEvents(new PickUpListener(), this);
        this.getCommand("addcontainer").setExecutor(new ContainerCommand());
        this.getCommand("removecontainer").setExecutor(new RemoveCommand());
    }

    private void checkDependenciesPlugin() {
        // Check if have GOOP for container
        Plugin goopPlugin = getServer().getPluginManager().getPlugin("Gunging_Ootilities_Plugin");
        if (goopPlugin == null) {
            plugin.getLogger().warning("Goop plugin not found. Disabling...");
            this.setEnabled(false);
        }
        // Check if have MMO Items
        Plugin mmoitems = getServer().getPluginManager().getPlugin("MMOItems");
        if (mmoitems != null) {
            plugin.getLogger().info("MMOItems plugin found.");
            mmoItemsEnabled = true;
        } else {
            plugin.getLogger().info("MMOItems plugin not found.");
            this.setEnabled(false);
        }
    }

    private void checkDebug(){
        debug = this.getConfig().getBoolean("debug");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
