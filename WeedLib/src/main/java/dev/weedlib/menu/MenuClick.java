package dev.weedlib.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public record MenuClick(Player player, int slot, ClickType clickType, ItemStack clickedItem) {}
