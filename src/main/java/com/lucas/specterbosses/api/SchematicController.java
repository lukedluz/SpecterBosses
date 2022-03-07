package com.tke.boss.api;

import com.tke.boss.*;
import com.sk89q.worldedit.bukkit.*;
import com.sk89q.worldedit.extent.clipboard.io.*;
import java.io.*;
import com.sk89q.worldedit.extent.*;
import com.sk89q.worldedit.math.transform.*;
import com.sk89q.worldedit.function.operation.*;
import org.bukkit.*;
import com.sk89q.worldedit.world.registry.*;
import com.sk89q.worldedit.extent.clipboard.*;
import com.sk89q.worldedit.*;

public class SchematicController
{
    String locationFolder;
    private static File file;
    
    public SchematicController() {
        this.locationFolder = BossArena.getInstance().getDataFolder().getAbsolutePath() + "/bossArena.schematic";
        SchematicController.file = new File(this.locationFolder);
    }
    
    public void paste(final Location location) {
        final World world = location.getWorld();
        location.add(0.0, 1.0, 0.0);
        final Vector to = new Vector(location.getX(), location.getY(), location.getZ());
        final BukkitWorld weWorld = new BukkitWorld(world);
        final WorldData worldData = weWorld.getWorldData();
        Clipboard clipboard = null;
        try {
            clipboard = ClipboardFormat.SCHEMATIC.getReader((InputStream)new FileInputStream(SchematicController.file)).read(worldData);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        final EditSession extent = WorldEdit.getInstance().getEditSessionFactory().getEditSession((LocalWorld)weWorld, -1);
        final AffineTransform transform = new AffineTransform();
        assert clipboard != null;
        final ForwardExtentCopy copy = new ForwardExtentCopy((Extent)clipboard, clipboard.getRegion(), clipboard.getOrigin(), (Extent)extent, to);
        if (!transform.isIdentity()) {
            copy.setTransform((Transform)transform);
        }
        try {
            Operations.completeLegacy((Operation)copy);
        }
        catch (MaxChangedBlocksException e2) {
            e2.printStackTrace();
        }
        extent.flushQueue();
    }
    
    static {
        SchematicController.file = null;
    }
}
