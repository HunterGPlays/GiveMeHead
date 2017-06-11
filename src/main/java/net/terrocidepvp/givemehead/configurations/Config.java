package net.terrocidepvp.givemehead.configurations;

import org.bukkit.plugin.*;
import net.terrocidepvp.givemehead.*;
import org.bukkit.*;
import java.util.logging.*;
import java.io.*;
import org.bukkit.configuration.file.*;

public class Config
{
    private final Plugin PLUGIN;
    private final String FILENAME;
    private final File FOLDER;
    private FileConfiguration config;
    private File configFile;
    
    public Config(final File folder, String filename, final GiveMeHead instance) {
        if (!filename.endsWith(".yml")) {
            filename += ".yml";
        }
        this.FILENAME = filename;
        this.PLUGIN = (Plugin)instance;
        this.FOLDER = folder;
        this.config = null;
        this.configFile = null;
        this.reload();
    }
    
    public Config(String filename, final GiveMeHead instance) {
        if (!filename.endsWith(".yml")) {
            filename += ".yml";
        }
        this.FILENAME = filename;
        this.PLUGIN = (Plugin)instance;
        this.FOLDER = this.PLUGIN.getDataFolder();
        this.config = null;
        this.configFile = null;
        this.reload();
    }
    
    public FileConfiguration getConfig() {
        if (this.config == null) {
            this.reload();
        }
        return this.config;
    }
    
    public Location getLocation(final String path) {
        final Location l = new Location(Bukkit.getWorld(this.getConfig().getString(path + ".w")), this.getConfig().getDouble(path + ".x"), this.getConfig().getDouble(path + ".y"), this.getConfig().getDouble(path + ".z"), Float.parseFloat("" + this.getConfig().getDouble(path + ".yaw")), Float.parseFloat("" + this.getConfig().getDouble(path + ".pitch")));
        return l;
    }
    
    public void reload() {
        if (!this.FOLDER.exists()) {
            try {
                if (this.FOLDER.mkdir()) {
                    this.PLUGIN.getLogger().log(Level.INFO, "Folder " + this.FOLDER.getName() + " created.");
                }
                else {
                    this.PLUGIN.getLogger().log(Level.WARNING, "Unable to create folder " + this.FOLDER.getName() + ".");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.configFile = new File(this.FOLDER, this.FILENAME);
        if (!this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.configFile);
    }
    
    public void save() {
        if (this.config == null || this.configFile == null) {
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        }
        catch (IOException ex) {
            this.PLUGIN.getLogger().log(Level.WARNING, "Could not save config to " + this.configFile.getName(), ex);
        }
    }
    
    public void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.PLUGIN.getDataFolder(), this.FILENAME);
        }
        if (!this.configFile.exists()) {
            this.PLUGIN.saveResource(this.FILENAME, false);
        }
    }
    
    public void set(final String path, final Object o) {
        this.getConfig().set(path, o);
    }
    
    public void setLocation(final String path, final Location l) {
        this.getConfig().set(path + ".w", (Object)l.getWorld().getName());
        this.getConfig().set(path + ".x", (Object)l.getX());
        this.getConfig().set(path + ".y", (Object)l.getY());
        this.getConfig().set(path + ".z", (Object)l.getZ());
        this.getConfig().set(path + ".yaw", (Object)l.getYaw());
        this.getConfig().set(path + ".pitch", (Object)l.getPitch());
        this.save();
    }
}
