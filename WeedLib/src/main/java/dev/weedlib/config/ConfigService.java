package dev.weedlib.config;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Creates and caches plugin-owned YAML files without duplicating file boilerplate. */
public final class ConfigService {
    private final JavaPlugin plugin;
    private final Map<String, ConfigFile> files = new ConcurrentHashMap<>();
    public ConfigService(JavaPlugin plugin) { this.plugin = plugin; }

    public ConfigFile open(String name) {
        String safe = validate(name);
        return files.computeIfAbsent(safe, key -> {
            File file = new File(plugin.getDataFolder(), key);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) throw new IllegalStateException("Cannot create config directory: " + parent);
            if (!file.exists()) {
                try { if (!file.createNewFile()) throw new IllegalStateException("Cannot create config file: " + file); }
                catch (java.io.IOException exception) { throw new IllegalStateException("Cannot create config file: " + file, exception); }
            }
            return new ConfigFile(file);
        });
    }
    public void reloadAll() { files.values().forEach(ConfigFile::reload); }
    private String validate(String name) {
        if (name == null || name.isBlank() || name.contains("..") || name.contains(":") || name.indexOf('\0') >= 0
                || name.startsWith("/") || name.startsWith("\\") || !(name.endsWith(".yml") || name.endsWith(".yaml")))
            throw new IllegalArgumentException("Config name must be a relative .yml/.yaml path inside the plugin data folder");
        return name.replace('\\', '/');
    }
}
