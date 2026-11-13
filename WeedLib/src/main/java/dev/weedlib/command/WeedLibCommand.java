package dev.weedlib.command;

import dev.weedlib.WeedLib;
import org.bukkit.command.*;
import java.util.*;

public final class WeedLibCommand implements TabExecutor {
    private final WeedLib plugin;
    public WeedLibCommand(WeedLib plugin) { this.plugin = plugin; }

    @Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) { sender.sendMessage("§8[§bWeedLib§8] §7В доступе отказано."); return true; }
        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            sender.sendMessage("§8§m──────── §bWeedLib §8§m────────");
            sender.sendMessage("§7Версия: §f" + plugin.getDescription().getVersion());
            sender.sendMessage("§7Назначение: §fAPI-библиотека для плагинов");
            sender.sendMessage("§7Модули: §fItems · PDC/NBT · Models · Animations · Placeholders");
            sender.sendMessage("§8§m────────────────────────"); return true;
        }
        sender.sendMessage(plugin.message("&f/weedlib info &8— &7информация о библиотеке"));
        return true;
    }

    @Override public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.isOp()) return Collections.emptyList();
        if (args.length == 1) return filter(List.of("info"), args[0]);
        return Collections.emptyList();
    }
    private List<String> filter(List<String> values, String prefix) {
        return values.stream().filter(v -> v.regionMatches(true, 0, prefix, 0, prefix.length())).toList();
    }
}
