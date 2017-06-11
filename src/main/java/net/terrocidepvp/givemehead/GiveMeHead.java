package net.terrocidepvp.givemehead;

import org.bukkit.plugin.java.*;
import org.bukkit.event.*;
import net.terrocidepvp.givemehead.commands.*;
import org.bukkit.command.*;
import net.terrocidepvp.givemehead.configurations.*;
import org.bukkit.plugin.*;
import net.terrocidepvp.givemehead.utils.*;
import net.terrocidepvp.givemehead.hooks.*;
import net.terrocidepvp.givemehead.listeners.*;
import java.io.*;

public class GiveMeHead extends JavaPlugin implements Listener
{
    public static GiveMeHead plugin;
    private static GiveMeHead instance;
    public int[] serverVersion;
    
    public static GiveMeHead getInstance() {
        return GiveMeHead.instance;
    }
    
    public void onDisable() {
        this.getLogger().info("Disabled!");
    }
    
    public void onEnable() {
        super.onEnable();
        GiveMeHead.plugin = this;
        GiveMeHead.instance = this;
        this.getCommand("behead").setExecutor((CommandExecutor)new CommandManager());
        this.getCommand("beheading").setExecutor((CommandExecutor)new CommandManager());
        this.getCommand("givehead").setExecutor((CommandExecutor)new CommandManager());
        this.getCommand("head").setExecutor((CommandExecutor)new CommandManager());
        this.getCommand("givemehead").setExecutor((CommandExecutor)new CommandManager());
        this.getCommand("gmh").setExecutor((CommandExecutor)new CommandManager());
        this.setupConfig();
        final Config c = new Config("locations", GiveMeHead.plugin);
        c.saveDefaultConfig();
        this.getLogger().info("Checking if the config is broken...");
        if (!this.getConfig().isSet("config-version")) {
            this.getLogger().severe("The config.yml file is broken!");
            this.getLogger().severe(" The plugin failed to detect a 'config-version'.");
            this.getLogger().severe("The plugin will not load until you generate a new, working config OR if you fix the config.");
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        this.getLogger().info("The config is not broken.");
        this.getLogger().info("Checking if the config is outdated...");
        final int ver = 5;
        if (this.getConfig().getInt("config-version") != 5) {
            this.getLogger().severe("Your config is outdated!");
            this.getLogger().severe("The plugin will not load unless you change the config version to 5.");
            this.getLogger().severe("This means that you will need to reset your config, as there may have been major changes to the plugin.");
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        this.getLogger().info("The config was not detected as outdated.");
        this.serverVersion = VersionUtil.getMCVersion(this.getServer().getVersion());
        this.getLogger().info("Running server version " + Integer.toString(this.serverVersion[0]) + "." + Integer.toString(this.serverVersion[1]));
        this.getLogger().info("Checking if Vault is enabled.");
        if (this.getServer().getPluginManager().isPluginEnabled("Vault")) {
            this.getLogger().info("Vault is enabled - attempting to hook...");
            if (!VaultHook.setupEconomy()) {
                this.getLogger().severe("Disabled due to no Vault dependency found!");
                this.getServer().getPluginManager().disablePlugin((Plugin)this);
                return;
            }
            VaultHook.setupPermissions();
            VaultHook.setupChat();
        }
        else {
            this.getLogger().severe("Vault was not found! Economy functions (such as head hunting) will not work.");
        }
        this.getLogger().info("Loading kill listener...");
        new KillListener((Plugin)this);
        this.getLogger().info("Enabled!");
    }
    
    private void setupConfig() {
        try {
            if (!this.getDataFolder().exists()) {
                this.getDataFolder().mkdirs();
            }
            final File file = new File(this.getDataFolder(), "config.yml");
            if (!file.exists()) {
                this.getLogger().info("Couldn't find a configuration file. Attempting to make one now.");
                this.saveDefaultConfig();
                this.reloadConfig();
            }
            else {
                this.getLogger().info("Configuration file found.");
            }
        }
        catch (Exception e) {
            this.getLogger().severe("*** STACK TRACE START ***");
            e.printStackTrace();
            this.getLogger().severe("*** STACK TRACE END ***");
        }
    }
}
