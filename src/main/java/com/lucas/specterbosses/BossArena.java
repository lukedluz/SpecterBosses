package com.tke.boss;

import org.bukkit.plugin.java.*;
import com.tke.boss.controller.*;
import org.bukkit.*;
import com.tke.boss.listener.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import com.tke.boss.command.*;
import org.bukkit.command.*;
import com.tke.boss.api.*;

public class BossArena extends JavaPlugin
{
    private static BossArena instance;
    private BossController bossController;
    private BossTypeController bossTypeController;
    private ParticleController particleController;
    private Plot plotAPI;
    
    public void onEnable() {
        (BossArena.instance = this).init();
        Bukkit.getPluginManager().registerEvents((Listener)new BossListener(), (Plugin)this);
        final Comando cmd = new Comando(this);
        this.getCommand("giveboss").setExecutor((CommandExecutor)cmd);
        this.getCommand("setitems").setExecutor((CommandExecutor)cmd);
    }
    
    private void init() {
        new Config("bossArena.schematic").saveDefaultConfig();
        this.bossController = new BossController();
        this.bossTypeController = new BossTypeController();
        this.particleController = new ParticleController();
        this.plotAPI = new Plot();
    }
    
    public void onDisable() {
        this.getBossController().unload();
    }
    
    public static BossArena getInstance() {
        return BossArena.instance;
    }
    
    public BossController getBossController() {
        return this.bossController;
    }
    
    public BossTypeController getBossTypeController() {
        return this.bossTypeController;
    }
    
    public ParticleController getParticleController() {
        return this.particleController;
    }
    
    public Plot getPlotAPI() {
        return this.plotAPI;
    }
}
