package dev.weedlib.menu;

import org.bukkit.Bukkit;
import dev.weedlib.text.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/** Central listener and factory for menus created through WeedLib. */
public final class MenuService implements Listener {
    private final JavaPlugin plugin;
    public MenuService(JavaPlugin plugin) { this.plugin = plugin; plugin.getServer().getPluginManager().registerEvents(this, plugin); }
    public MenuBuilder create(String title, int rows) { return new MenuBuilder(this, title, rows); }

    void open(Player player, String title, int size, Map<Integer, ItemStack> items, Map<Integer, Consumer<MenuClick>> actions) {
        MenuHolder holder = new MenuHolder(new HashMap<>(actions));
        Inventory inventory = Bukkit.createInventory(holder, size, Text.color(title));
        holder.inventory = inventory;
        items.forEach((slot, item) -> inventory.setItem(slot, item.clone()));
        player.openInventory(inventory);
    }

    @EventHandler public void onClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof MenuHolder holder)) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;
        int rawSlot = event.getRawSlot();
        if (rawSlot < 0 || rawSlot >= event.getView().getTopInventory().getSize()) return;
        Consumer<MenuClick> action = holder.actions.get(rawSlot);
        if (action != null) {
            ItemStack item = event.getCurrentItem();
            action.accept(new MenuClick(player, rawSlot, event.getClick(), item == null ? null : item.clone()));
        }
    }
    @EventHandler public void onDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof MenuHolder) event.setCancelled(true);
    }

    private static final class MenuHolder implements InventoryHolder {
        private final Map<Integer, Consumer<MenuClick>> actions;
        private Inventory inventory;
        private MenuHolder(Map<Integer, Consumer<MenuClick>> actions) { this.actions = actions; }
        @Override public Inventory getInventory() { return inventory; }
    }
}
