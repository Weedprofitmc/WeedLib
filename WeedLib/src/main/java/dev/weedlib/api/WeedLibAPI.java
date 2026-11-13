package dev.weedlib.api;
import dev.weedlib.WeedLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import java.util.Optional;
public final class WeedLibAPI {
 private WeedLibAPI(){}
 public static Optional<WeedLib> get(){ Plugin p=Bukkit.getPluginManager().getPlugin("WeedLib"); return p instanceof WeedLib w && w.isEnabled()?Optional.of(w):Optional.empty(); }
}
