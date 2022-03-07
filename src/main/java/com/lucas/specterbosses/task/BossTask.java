package com.tke.boss.task;

import org.bukkit.scheduler.*;
import com.tke.boss.models.*;
import org.bukkit.*;
import com.tke.boss.*;
import org.bukkit.plugin.*;
import com.tke.boss.api.*;
import org.bukkit.util.*;
import org.bukkit.entity.*;
import java.util.*;

public class BossTask extends BukkitRunnable
{
    private Boss boss;
    private SkullReflection skull;
    private int time;
    Random random;
    
    public BossTask(final Boss boss) {
        this.time = 0;
        this.random = new Random();
        this.boss = boss;
        this.skull = boss.getSkull();
    }
    
    public void run() {
        float yaw = (float)(this.skull.getLocation().getYaw() + 5.0);
        if (yaw >= 180.0f) {
            yaw *= -1.0f;
        }
        this.skull.setHelmet(new ItemBuilder().skull(this.boss.getBossType().getTexture()).getItem());
        final Location loc = this.skull.getLocation().clone();
        loc.setYaw(yaw);
        this.skull.teleport(loc);
        this.checkAttacked();
        ++this.time;
    }
    
    private void checkAttacked() {
        final List<Player> targets = this.getTargets();
        if (targets.isEmpty()) {
            this.boss.isSafe();
        }
        else {
            this.boss.setAttacked();
        }
        if (this.time >= 40) {
            this.time = 0;
            final boolean critico = this.random.nextInt(3) == 1;
            double damage = this.boss.getBossType().getDamage();
            if (critico) {
                damage *= 2.3;
            }
            final double finalDamage = damage;
            targets.forEach(d -> new BukkitRunnable() {
                final /* synthetic */ Player val$d;
                final /* synthetic */ double val$finalDamage;
                
                public void run() {
                    this.val$d.damage(this.val$finalDamage);
                    if (BossTask.this.random.nextInt(3) == 1) {
                        BossTask.this.knockback(this.val$d);
                    }
                    this.cancel();
                }
            }.runTask((Plugin)BossArena.getInstance()));
        }
    }
    
    private void knockback(final Player player) {
        final Vector directional = player.getLocation().getDirection().normalize().setY(0).multiply(-2);
        player.setVelocity(directional);
        ParticleEffect.SMOKE_LARGE.display(0.0f, 0.0f, 0.0f, 0.0f, 100, player.getLocation());
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
    
    public Boss getBoss() {
        return this.boss;
    }
    
    public SkullReflection getSkull() {
        return this.skull;
    }
}
