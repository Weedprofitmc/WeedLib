package dev.weedlib.item;

import dev.weedlib.WeedLib;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

import java.util.List;

/** PersistentDataContainer-backed item tags and model helpers. */
public final class ItemService {
    private final WeedLib plugin;
    public ItemService(WeedLib plugin) { this.plugin = plugin; reload(); }
    private YamlConfiguration models;
    public void reload() { models = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "items.yml")); }
    public ItemStack applyModel(ItemStack item, String alias) {
        ensureEnabled();
        if (models == null) reload();
        String path = "models." + alias;
        if (!models.contains(path)) return null;
        String materialName = models.getString(path + ".material", "");
        if (!materialName.isBlank() && !item.getType().name().equalsIgnoreCase(materialName)) return null;
        if (models.contains(path + ".custom-model-data")) setCustomModel(item, models.getInt(path + ".custom-model-data"));
        String id = models.getString(path + ".itemsadder-id");
        if (id == null || id.isBlank()) return item;
        ItemStack custom = itemsAdderItem(id);
        return custom == null ? item : custom;
    }
    private void ensureEnabled() { if (!plugin.getConfig().getBoolean("modules.items", true)) throw new IllegalStateException("WeedLib items module is disabled"); }
    public ItemStack create(Material material, String displayName, List<String> lore) {
        ensureEnabled();
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (displayName != null) meta.setDisplayName(org.bukkit.ChatColor.translateAlternateColorCodes('&', displayName));
        if (lore != null) meta.setLore(lore.stream().map(s -> org.bukkit.ChatColor.translateAlternateColorCodes('&', s)).toList());
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack setStringTag(ItemStack item, String key, String value) {
        ensureEnabled();
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key(key), PersistentDataType.STRING, value);
        item.setItemMeta(meta); return item;
    }
    public String getStringTag(ItemStack item, String key) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(key(key), PersistentDataType.STRING);
    }
    public boolean hasTag(ItemStack item, String key) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key(key));
    }
    public ItemStack setIntegerTag(ItemStack item, String key, int value) {
        ensureEnabled();
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key(key), PersistentDataType.INTEGER, value);
        item.setItemMeta(meta); return item;
    }
    public Integer getIntegerTag(ItemStack item, String key) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(key(key), PersistentDataType.INTEGER);
    }
    /** Applies vanilla custom model data; ItemsAdder can consume the matching configured model ID. */
    public ItemStack setCustomModel(ItemStack item, int modelId) {
        ensureEnabled();
        if (item == null || item.getType().isAir()) return item;
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(modelId);
        item.setItemMeta(meta); return item;
    }
    public ItemStack clearCustomModel(ItemStack item) {
        ensureEnabled();
        if (item == null || item.getType().isAir()) return item;
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(null);
        item.setItemMeta(meta); return item;
    }
    /** Creates a configured ItemsAdder item through its public API using reflection (optional dependency). */
    public ItemStack itemsAdderItem(String namespacedId) {
        ensureEnabled();
        if (plugin.getServer().getPluginManager().getPlugin("ItemsAdder") == null) return null;
        try {
            Class<?> api = Class.forName("dev.lone.itemsadder.api.CustomStack");
            Object customStack = api.getMethod("getInstance", String.class).invoke(null, namespacedId);
            if (customStack == null) return null;
            return (ItemStack) api.getMethod("getItemStack").invoke(customStack);
        } catch (ReflectiveOperationException ex) {
            plugin.getLogger().warning("ItemsAdder API lookup failed for " + namespacedId + ": " + ex.getMessage());
            return null;
        }
    }
    public ItemStack setLongTag(ItemStack item, String key, long value) {
        ensureEnabled();
        ItemMeta meta=item.getItemMeta(); meta.getPersistentDataContainer().set(key(key), PersistentDataType.LONG, value); item.setItemMeta(meta); return item;
    }
    public Long getLongTag(ItemStack item, String key) {
        if(item==null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(key(key), PersistentDataType.LONG);
    }
    public ItemStack setDoubleTag(ItemStack item, String key, double value) {
        ensureEnabled();
        if(!Double.isFinite(value)) throw new IllegalArgumentException("Tag value must be finite");
        ItemMeta meta=item.getItemMeta(); meta.getPersistentDataContainer().set(key(key), PersistentDataType.DOUBLE, value); item.setItemMeta(meta); return item;
    }
    public Double getDoubleTag(ItemStack item, String key) {
        if(item==null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(key(key), PersistentDataType.DOUBLE);
    }
    public ItemStack removeTag(ItemStack item, String key) {
        ensureEnabled();
        if(item==null || !item.hasItemMeta()) return item;
        ItemMeta meta=item.getItemMeta(); meta.getPersistentDataContainer().remove(key(key)); item.setItemMeta(meta); return item;
    }
    private NamespacedKey key(String key) { return new NamespacedKey(plugin, key.toLowerCase(java.util.Locale.ROOT)); }
}
