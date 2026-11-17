package dev.weedlib.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/** Reusable, cached YAML file wrapper. */
public final class ConfigFile {
    private final File file;
    private YamlConfiguration yaml;

    ConfigFile(File file) { this.file = file; reload(); }
    public void reload() { yaml = YamlConfiguration.loadConfiguration(file); }
    public void save() {
        try { yaml.save(file); }
        catch (IOException exception) { throw new IllegalStateException("Could not save config " + file.getName(), exception); }
    }
    public String getString(String path) { return yaml.getString(path); }
    public String getString(String path, String fallback) { return yaml.getString(path, fallback); }
    public int getInt(String path, int fallback) { return yaml.getInt(path, fallback); }
    public long getLong(String path, long fallback) { return yaml.getLong(path, fallback); }
    public boolean getBoolean(String path, boolean fallback) { return yaml.getBoolean(path, fallback); }
    public List<String> getStringList(String path) { return yaml.getStringList(path); }
    public boolean contains(String path) { return yaml.contains(path); }
    public ConfigurationSection getSection(String path) { return yaml.getConfigurationSection(path); }
    public void set(String path, Object value) { yaml.set(path, value); }
    public YamlConfiguration handle() { return yaml; }
    public File file() { return file; }
}
