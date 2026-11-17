package dev.weedlib;

import dev.weedlib.animation.ItemAnimation;
import dev.weedlib.cooldown.CooldownService;
import dev.weedlib.item.ItemService;
import dev.weedlib.config.ConfigService;
import dev.weedlib.menu.MenuService;
import dev.weedlib.placeholder.WeedPlaceholderExpansion;
import dev.weedlib.task.TaskService;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/** Standalone reusable developer library. It intentionally has no dependency on WeedCore. */
public final class WeedLib extends JavaPlugin {
    private ItemService items;
    private ItemAnimation animations;
    private CooldownService cooldowns;
    private TaskService tasks;
    private ConfigService configs;
    private MenuService menus;

    @Override public void onEnable() {
        saveDefaultConfig();
        if (!new java.io.File(getDataFolder(), "items.yml").exists()) saveResource("items.yml", false);
        items = new ItemService(this);
        animations = new ItemAnimation(this);
        cooldowns = new CooldownService();
        tasks = new TaskService(this);
        configs = new ConfigService(this);
        menus = new MenuService(this);
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new WeedPlaceholderExpansion(this).register();
            getLogger().info("PlaceholderAPI integration enabled.");
        }
        getLogger().info("WeedLib enabled: reusable developer services are ready; no gameplay mechanics or commands are registered.");
    }

    public ItemService items() { return items; }
    public ItemAnimation animations() { return animations; }
    public CooldownService cooldowns() { return cooldowns; }
    public TaskService tasks() { return tasks; }
    public ConfigService configs() { return configs; }
    public MenuService menus() { return menus; }
    public String message(String text) { return ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix", "&8[&bWeedLib&8] &7") + text); }
}
