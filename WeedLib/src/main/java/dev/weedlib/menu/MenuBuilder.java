package dev.weedlib.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/** Small inventory-menu builder with per-slot callbacks and safe item cloning. */
public final class MenuBuilder {
    private final MenuService service;
    private final String title;
    private final int size;
    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final Map<Integer, Consumer<MenuClick>> actions = new HashMap<>();

    MenuBuilder(MenuService service, String title, int rows) {
        this.service = service;
        this.title = Objects.requireNonNull(title, "title");
        if (rows < 1 || rows > 6) throw new IllegalArgumentException("Menu rows must be between 1 and 6");
        this.size = rows * 9;
    }
    public MenuBuilder item(int slot, ItemStack item) { checkSlot(slot); items.put(slot, Objects.requireNonNull(item, "item").clone()); return this; }
    public MenuBuilder onClick(int slot, Consumer<MenuClick> action) { checkSlot(slot); actions.put(slot, Objects.requireNonNull(action, "action")); return this; }
    public MenuBuilder button(int slot, ItemStack item, Consumer<MenuClick> action) { return item(slot, item).onClick(slot, action); }
    public void open(Player player) { service.open(player, title, size, items, actions); }
    private void checkSlot(int slot) { if (slot < 0 || slot >= size) throw new IndexOutOfBoundsException("Slot " + slot + " is outside menu size " + size); }
}
