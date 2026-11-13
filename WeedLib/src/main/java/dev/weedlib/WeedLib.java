package dev.weedlib;

import dev.weedlib.animation.ItemAnimation;
import dev.weedlib.command.WeedLibCommand;
import dev.weedlib.item.ItemService;
import dev.weedlib.placeholder.WeedPlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class WeedLib extends JavaPlugin {
    private ItemService items;
    private ItemAnimation animations;

    @Override public void onEnable() {
        saveDefaultConfig();
        if (!getConfig().getBoolean("modules.items", true)) getLogger().info("Item module is disabled.");
        items = new ItemService(this);
        animations = new ItemAnimation(this);
        getCommand("weedlib").setExecutor(new WeedLibCommand(this));
        getCommand("weedlib").setTabCompleter((org.bukkit.command.TabCompleter) getCommand("weedlib").getExecutor());
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new WeedPlaceholderExpansion(this).register();
            getLogger().info("PlaceholderAPI integration enabled.");
        }
        getLogger().info("WeedLib API is ready.");
    }

    public ItemService items() { return items; }
    public ItemAnimation animations() { return animations; }
    public String message(String text) {
        return ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("prefix", "&8[&bWeedLib&8] &7") + text);
    }
}
