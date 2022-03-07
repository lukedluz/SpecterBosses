package com.tke.boss.util;

import org.bukkit.*;
import org.bukkit.entity.*;
import com.tke.boss.*;

public class Util
{
    public static void remove(final Location center) {
        for (int x = 0; x < 7; ++x) {
            for (int y = -1; y < 6; ++y) {
                for (int z = 0; z < 7; ++z) {
                    final Location block = new Location(center.getWorld(), center.getX() - 3.0 + x, center.getY() + y, center.getZ() - 3.0 + z);
                    if (block.getBlock() != null) {
                        block.getBlock().setTypeIdAndData(0, (byte)0, false);
                    }
                }
            }
        }
    }
    
    public static boolean check(final Player player, final Location center) {
        for (int x = 0; x < 7; ++x) {
            for (int y = -1; y < 6; ++y) {
                for (int z = 0; z < 7; ++z) {
                    if (!BossArena.getInstance().getPlotAPI().isBuild(player, new Location(center.getWorld(), center.getX() - 3.0 + x, center.getY() + y, center.getZ() - 3.0 + z))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
