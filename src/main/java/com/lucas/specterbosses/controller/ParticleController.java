package com.tke.boss.controller;

import org.bukkit.*;
import org.bukkit.scheduler.*;
import com.tke.boss.api.*;
import com.tke.boss.*;
import org.bukkit.plugin.*;
import org.bukkit.util.*;

public class ParticleController
{
    public void waves(final Location center) {
        new BukkitRunnable() {
            double g = 0.0;
            Location loc = center.clone();
            
            public void run() {
                this.g += 0.3141592653589793;
                for (double jh = 0.0; jh < 6.283185307179586; jh += 0.09817477042468103) {
                    final double x = this.g * Math.cos(jh);
                    final double y = Math.exp(-0.1 * this.g) * Math.sin(this.g) + 1.5;
                    final double z = this.g * Math.sin(jh);
                    this.loc.add(x, y, z);
                    ParticleEffect.FIREWORKS_SPARK.display(0.0f, 0.0f, 0.0f, 0.0f, 1, this.loc);
                    this.loc.subtract(x, y, z);
                }
                if (this.g > 15.0) {
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)BossArena.getInstance(), 0L, 1L);
    }
    
    public void directional(final Location center, final Vector direction) {
        new BukkitRunnable() {
            double g = 0.0;
            
            public void run() {
                ParticleEffect.SMOKE_LARGE.display(direction, 1.0f, center);
                this.cancel();
            }
        }.runTaskTimer((Plugin)BossArena.getInstance(), 0L, 1L);
    }
}
