package com.tke.boss.controller;

import org.bukkit.*;
import com.tke.boss.models.*;
import com.tke.boss.*;
import org.bukkit.entity.*;

public class BossController
{
    public void spawn(final Location location, final BossType bossType) {
        final Boss boss = new Boss(bossType, location);
        BossArena.getInstance().getParticleController().waves(location.clone().subtract(0.0, 1.0, 0.0));
    }
    
    public Boss getBoss(final ArmorStand armorStand) {
        return Boss.bossIndex.get(armorStand);
    }
    
    public void unload() {
        final Location center;
        int x;
        int y;
        int z;
        Location loc;
        Boss.bossIndex.forEach((stand, boss) -> {
            stand.remove();
            center = boss.getLocation();
            for (x = 0; x < 7; ++x) {
                for (y = -1; y < 6; ++y) {
                    for (z = 0; z < 7; ++z) {
                        loc = new Location(center.getWorld(), center.getX() - 3.0 + x, center.getY() + y, center.getZ() - 3.0 + z);
                        loc.getBlock().setTypeIdAndData(0, (byte)0, false);
                    }
                }
            }
        });
    }
}
