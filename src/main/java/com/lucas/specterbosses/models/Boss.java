package com.tke.boss.models;

import com.tke.boss.task.*;
import com.tke.boss.*;
import org.bukkit.plugin.*;
import org.bukkit.scheduler.*;
import com.tke.boss.api.*;
import com.tke.boss.util.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.*;
import java.util.function.*;
import org.bukkit.entity.*;
import java.util.*;

public class Boss
{
    BossType bossType;
    double heath;
    Location location;
    BukkitTask runnable;
    SkullReflection skull;
    BukkitTask attackedTask;
    BukkitTask safeTask;
    boolean attacked;
    public static Map<ArmorStand, Boss> bossIndex;
    
    public Boss(final BossType bossType, final Location location) {
        this.bossType = bossType;
        this.location = location;
        this.heath = bossType.getHeath();
        (this.skull = new SkullReflection(location, this.toText())).spawn();
        this.skull.setHelmet(new ItemBuilder().skull(this.bossType.texture).getItem());
        Boss.bossIndex.put(this.skull.stand, this);
        this.runnable = new BossTask(this).runTaskTimerAsynchronously((Plugin)BossArena.getInstance(), 0L, 1L);
        this.attacked = false;
    }
    
    public void setAttacked() {
        if (this.attacked) {
            return;
        }
        if (this.attackedTask != null) {
            this.attackedTask.cancel();
        }
        this.attacked = true;
        this.skull.teleport(this.location.clone());
        this.attackedTask = new BukkitRunnable() {
            int subiu = 0;
            boolean subir = true;
            
            public void run() {
                if (this.subiu >= 15) {
                    this.subiu = 0;
                    this.subir = !this.subir;
                }
                final Location loc = Boss.this.skull.getLocation().clone();
                Boss.this.skull.teleport(loc.add(0.0, this.subir ? 0.15 : -0.15, 0.0));
                ++this.subiu;
            }
        }.runTaskTimerAsynchronously((Plugin)BossArena.getInstance(), 0L, 2L);
    }
    
    public void isSafe() {
        if (!this.attacked) {
            return;
        }
        if (this.safeTask != null) {
            this.safeTask.cancel();
        }
        if (this.attackedTask != null) {
            this.attackedTask.cancel();
        }
        this.attacked = false;
        this.safeTask = new BukkitRunnable() {
            double defaultY = Boss.this.getLocation().getY();
            
            public void run() {
                final Location loc = Boss.this.skull.getLocation().clone();
                final double y = loc.getY();
                Boss.this.skull.teleport(loc.subtract(0.0, 0.1, 0.0));
                if (y <= this.defaultY) {
                    this.cancel();
                    Boss.this.safeTask = null;
                }
            }
        }.runTaskTimerAsynchronously((Plugin)BossArena.getInstance(), 0L, 2L);
    }
    
    public String toText() {
        return this.bossType.getDisplayName() + " §e" + (int)this.heath + "§f/§a" + (int)this.bossType.getHeath() + " §c\u2764";
    }
    
    public void damage(final Player player, final double path) {
        final double newHealth = this.getHeath() - path;
        if (newHealth <= 0.0) {
            this.animateFinish(player);
        }
        else {
            this.setHeath(newHealth);
            this.skull.setLines(this.toText());
            this.sendParticleDamage((int)path);
        }
    }
    
    private void animateFinish(final Player matou) {
        ParticleEffect.EXPLOSION_HUGE.display(1.0f, 1.0f, 1.0f, 0.0f, 5, this.getLocation());
        this.getLocation().getWorld().strikeLightningEffect(this.getLocation());
        Util.remove(this.getLocation());
        Boss.bossIndex.remove(this.skull.stand, this);
        this.skull.remove();
        if (this.safeTask != null) {
            this.safeTask.cancel();
        }
        if (this.attackedTask != null) {
            this.attackedTask.cancel();
        }
        this.runnable.cancel();
        if (this.getBossType().items != null) {
            final Location c = this.getLocation().clone().subtract(0.0, 1.0, 0.0);
            c.getBlock().setType(Material.CHEST);
            final Chest chest = (Chest)c.getBlock().getState();
            chest.getBlockInventory().setContents(this.bossType.items);
        }
        this.bossType.getCommands().forEach(d -> Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), d.replace("@player", matou.getName())));
    }
    
    private void sendParticleDamage(final int damage) {
        final List<SkullReflection> armors = new ArrayList<SkullReflection>();
        final Random random = new Random();
        for (int i = 0; i < 5; ++i) {
            final Location loc = this.getLocation().clone().add((double)random.nextInt(3), (double)random.nextInt(3), (double)random.nextInt(3));
            final SkullReflection h = new SkullReflection(loc, "§7" + damage + " §c\u2764");
            h.spawn();
            armors.add(h);
        }
        this.getTargets().forEach(d -> d.playSound(d.getLocation(), Sound.WITHER_HURT, 0.3f, 1.0f));
        new BukkitRunnable() {
            public void run() {
                armors.forEach(SkullReflection::remove);
                this.cancel();
            }
        }.runTaskLater((Plugin)BossArena.getInstance(), 5L);
    }
    
    private List<Player> getTargets() {
        final Location loc = this.skull.getLocation().clone();
        final List<Player> pls = new ArrayList<Player>();
        for (final Entity ents : loc.getWorld().getNearbyEntities(loc, 5.0, 5.0, 5.0)) {
            if (ents instanceof Player) {
                final Player p = (Player)ents;
                pls.add(p);
            }
        }
        return pls;
    }
    
    public void setBossType(final BossType bossType) {
        this.bossType = bossType;
    }
    
    public void setLocation(final Location location) {
        this.location = location;
    }
    
    public void setSkull(final SkullReflection skull) {
        this.skull = skull;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Boss)) {
            return false;
        }
        final Boss other = (Boss)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$bossType = this.getBossType();
        final Object other$bossType = other.getBossType();
        Label_0065: {
            if (this$bossType == null) {
                if (other$bossType == null) {
                    break Label_0065;
                }
            }
            else if (this$bossType.equals(other$bossType)) {
                break Label_0065;
            }
            return false;
        }
        if (Double.compare(this.getHeath(), other.getHeath()) != 0) {
            return false;
        }
        final Object this$location = this.getLocation();
        final Object other$location = other.getLocation();
        Label_0118: {
            if (this$location == null) {
                if (other$location == null) {
                    break Label_0118;
                }
            }
            else if (this$location.equals(other$location)) {
                break Label_0118;
            }
            return false;
        }
        final Object this$runnable = this.getRunnable();
        final Object other$runnable = other.getRunnable();
        Label_0155: {
            if (this$runnable == null) {
                if (other$runnable == null) {
                    break Label_0155;
                }
            }
            else if (this$runnable.equals(other$runnable)) {
                break Label_0155;
            }
            return false;
        }
        final Object this$skull = this.getSkull();
        final Object other$skull = other.getSkull();
        Label_0192: {
            if (this$skull == null) {
                if (other$skull == null) {
                    break Label_0192;
                }
            }
            else if (this$skull.equals(other$skull)) {
                break Label_0192;
            }
            return false;
        }
        final Object this$attackedTask = this.getAttackedTask();
        final Object other$attackedTask = other.getAttackedTask();
        Label_0229: {
            if (this$attackedTask == null) {
                if (other$attackedTask == null) {
                    break Label_0229;
                }
            }
            else if (this$attackedTask.equals(other$attackedTask)) {
                break Label_0229;
            }
            return false;
        }
        final Object this$safeTask = this.getSafeTask();
        final Object other$safeTask = other.getSafeTask();
        if (this$safeTask == null) {
            if (other$safeTask == null) {
                return this.isAttacked() == other.isAttacked();
            }
        }
        else if (this$safeTask.equals(other$safeTask)) {
            return this.isAttacked() == other.isAttacked();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Boss;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $bossType = this.getBossType();
        result = result * 59 + (($bossType == null) ? 0 : $bossType.hashCode());
        final long $heath = Double.doubleToLongBits(this.getHeath());
        result = result * 59 + (int)($heath >>> 32 ^ $heath);
        final Object $location = this.getLocation();
        result = result * 59 + (($location == null) ? 0 : $location.hashCode());
        final Object $runnable = this.getRunnable();
        result = result * 59 + (($runnable == null) ? 0 : $runnable.hashCode());
        final Object $skull = this.getSkull();
        result = result * 59 + (($skull == null) ? 0 : $skull.hashCode());
        final Object $attackedTask = this.getAttackedTask();
        result = result * 59 + (($attackedTask == null) ? 0 : $attackedTask.hashCode());
        final Object $safeTask = this.getSafeTask();
        result = result * 59 + (($safeTask == null) ? 0 : $safeTask.hashCode());
        result = result * 59 + (this.isAttacked() ? 79 : 97);
        return result;
    }
    
    @Override
    public String toString() {
        return "Boss(bossType=" + this.getBossType() + ", heath=" + this.getHeath() + ", location=" + this.getLocation() + ", runnable=" + this.getRunnable() + ", skull=" + this.getSkull() + ", attackedTask=" + this.getAttackedTask() + ", safeTask=" + this.getSafeTask() + ", attacked=" + this.isAttacked() + ")";
    }
    
    public BossType getBossType() {
        return this.bossType;
    }
    
    public double getHeath() {
        return this.heath;
    }
    
    public void setHeath(final double heath) {
        this.heath = heath;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public BukkitTask getRunnable() {
        return this.runnable;
    }
    
    public void setRunnable(final BukkitTask runnable) {
        this.runnable = runnable;
    }
    
    public SkullReflection getSkull() {
        return this.skull;
    }
    
    public BukkitTask getAttackedTask() {
        return this.attackedTask;
    }
    
    public BukkitTask getSafeTask() {
        return this.safeTask;
    }
    
    public void setAttackedTask(final BukkitTask attackedTask) {
        this.attackedTask = attackedTask;
    }
    
    public void setSafeTask(final BukkitTask safeTask) {
        this.safeTask = safeTask;
    }
    
    public boolean isAttacked() {
        return this.attacked;
    }
    
    public void setAttacked(final boolean attacked) {
        this.attacked = attacked;
    }
    
    static {
        Boss.bossIndex = new HashMap<ArmorStand, Boss>();
    }
}
