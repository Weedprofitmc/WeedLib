package dev.weedlib.cooldown;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
/** Thread-safe per-player cooldown storage. */
public final class CooldownService {
 private final ConcurrentHashMap<String,Long> expirations=new ConcurrentHashMap<>();
 public boolean has(UUID player,String key){Long end=expirations.get(id(player,key));if(end==null)return false;if(end<=System.currentTimeMillis()){expirations.remove(id(player,key),end);return false;}return true;}
 public long remainingMillis(UUID player,String key){Long end=expirations.get(id(player,key));if(end==null)return 0;long left=end-System.currentTimeMillis();if(left<=0){expirations.remove(id(player,key),end);return 0;}return left;}
 public void set(UUID player,String key,long duration,TimeUnit unit){if(duration<0)throw new IllegalArgumentException("duration cannot be negative");expirations.put(id(player,key),System.currentTimeMillis()+unit.toMillis(duration));}
 public boolean tryStart(UUID player,String key,long duration,TimeUnit unit){if(duration<0)throw new IllegalArgumentException("duration cannot be negative");String id=id(player,key);long now=System.currentTimeMillis(),end=now+unit.toMillis(duration);java.util.concurrent.atomic.AtomicBoolean started=new java.util.concurrent.atomic.AtomicBoolean(false);expirations.compute(id,(k,old)->{if(old==null||old<=now){started.set(true);return end;}return old;});return started.get();}
 public void clear(UUID player,String key){expirations.remove(id(player,key));}
 public void clear(UUID player){String prefix=player+":";expirations.keySet().removeIf(k->k.startsWith(prefix));}
 private String id(UUID player,String key){if(player==null||key==null||key.isBlank())throw new IllegalArgumentException("player and key are required");return player+":"+key.toLowerCase(java.util.Locale.ROOT);}
}
