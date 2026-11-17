package dev.weedlib.text;
import org.bukkit.ChatColor;
import java.util.List;
/** Common legacy-text helpers for plugin messages and item metadata. */
public final class Text {
 private Text(){}
 public static String color(String input){return input==null?"":ChatColor.translateAlternateColorCodes('&',input);}
 public static List<String> color(List<String> lines){return lines==null?List.of():lines.stream().map(Text::color).toList();}
 public static String strip(String input){return ChatColor.stripColor(color(input));}
 public static String join(String separator,String... lines){return color(String.join(separator,lines));}
}
