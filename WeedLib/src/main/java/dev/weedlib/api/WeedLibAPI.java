package dev.weedlib.api;

import dev.weedlib.WeedLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import java.util.Optional;

/** Static discovery helper for plugins that consume WeedLib. */
public final class WeedLibAPI {
    private WeedLibAPI() {}
    public static Optional<WeedLib> get() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WeedLib");
        return plugin instanceof WeedLib lib && lib.isEnabled() ? Optional.of(lib) : Optional.empty();
    }
    public static WeedLib require() {
        return get().orElseThrow(() -> new IllegalStateException("WeedLib is not enabled. Add depend: [WeedLib]."));
    }
}
