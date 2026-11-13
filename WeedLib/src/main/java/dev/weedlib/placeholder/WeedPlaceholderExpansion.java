package dev.weedlib.placeholder;
import dev.weedlib.WeedLib;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
public final class WeedPlaceholderExpansion extends PlaceholderExpansion {
 private final WeedLib plugin;
 public WeedPlaceholderExpansion(WeedLib plugin){this.plugin=plugin;}
 @Override public String getIdentifier(){return "weedlib";}
 @Override public String getAuthor(){return "Weed";}
 @Override public String getVersion(){return plugin.getDescription().getVersion();}
 @Override public boolean persist(){return true;}
 @Override public String onPlaceholderRequest(Player player,String params){
  return switch(params.toLowerCase()){
   case "tps" -> String.format(java.util.Locale.US,"%.2f",plugin.getServer().getTPS()[0]);
   case "tps_1m" -> String.format(java.util.Locale.US,"%.2f",plugin.getServer().getTPS()[0]);
   case "tps_5m" -> String.format(java.util.Locale.US,"%.2f",plugin.getServer().getTPS()[1]);
   case "tps_15m" -> String.format(java.util.Locale.US,"%.2f",plugin.getServer().getTPS()[2]);
   case "online" -> Integer.toString(plugin.getServer().getOnlinePlayers().size());
   case "max_players" -> Integer.toString(plugin.getServer().getMaxPlayers());
   case "ping" -> player==null?"":Integer.toString(player.getPing());
   default -> null;
  };
 }
}
