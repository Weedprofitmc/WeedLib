package dev.weedlib.animation;
import dev.weedlib.WeedLib;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
public final class ItemAnimation {
 private final WeedLib plugin;
 public ItemAnimation(WeedLib plugin){this.plugin=plugin;}
 /** Plays model-data frames on the main-hand item. repeats=0 means infinite. */
 public BukkitTask play(Player player,int[] frames,long periodTicks,int repeats){
  if(frames==null||frames.length==0||periodTicks<1||repeats<0) throw new IllegalArgumentException("Invalid animation settings");
  final int[] frame={0},cycles={0}; final BukkitTask[] task={null};
  task[0]=plugin.getServer().getScheduler().runTaskTimer(plugin,()->{
   if(!player.isOnline()){task[0].cancel();return;}
   ItemStack item=player.getInventory().getItemInMainHand();
   if(item.getType().isAir()) return;
   plugin.items().setCustomModel(item,frames[frame[0]++]);
   if(frame[0]>=frames.length){frame[0]=0;cycles[0]++;if(repeats>0&&cycles[0]>=repeats)task[0].cancel();}
  },0L,periodTicks);
  return task[0];
 }
}
