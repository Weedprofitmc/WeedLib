package dev.weedlib.task;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import java.util.concurrent.TimeUnit;
/** Small, consistently-owned scheduler facade. */
public final class TaskService {
 private final Plugin owner;
 public TaskService(Plugin owner){this.owner=owner;}
 public BukkitTask sync(Runnable task){return Bukkit.getScheduler().runTask(owner,task);}
 public BukkitTask later(Runnable task,long delay,TimeUnit unit){return Bukkit.getScheduler().runTaskLater(owner,task,Math.max(0,unit.toMillis(delay)/50));}
 public BukkitTask repeat(Runnable task,long initialDelay,long period,TimeUnit unit){long start=Math.max(0,unit.toMillis(initialDelay)/50),ticks=Math.max(1,unit.toMillis(period)/50);return Bukkit.getScheduler().runTaskTimer(owner,task,start,ticks);}
 public BukkitTask async(Runnable task){return Bukkit.getScheduler().runTaskAsynchronously(owner,task);}
}
