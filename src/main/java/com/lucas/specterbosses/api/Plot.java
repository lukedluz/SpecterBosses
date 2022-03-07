package com.tke.boss.api;

import com.intellectualcrafters.plot.api.*;
import com.tke.boss.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class Plot
{
    public PlotAPI api;
    
    public Plot() {
        final PluginManager manager = Bukkit.getServer().getPluginManager();
        final Plugin plotsquared = manager.getPlugin("PlotSquared");
        if (plotsquared != null && !plotsquared.isEnabled()) {
            manager.disablePlugin((Plugin)BossArena.getInstance());
            return;
        }
        this.api = new PlotAPI();
    }
    
    public boolean isBuild(final Player player, final Location location) {
        final com.intellectualcrafters.plot.object.Plot plot = this.api.getPlot(location);
        return plot != null && plot.getArea() != null && plot.getArea().contains((int)location.getX(), (int)location.getZ());
    }
}
