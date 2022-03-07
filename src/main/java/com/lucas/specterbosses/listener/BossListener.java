package com.tke.boss.listener;

import com.tke.boss.*;
import org.bukkit.event.block.*;
import com.tke.boss.util.*;
import com.tke.boss.api.*;
import org.bukkit.inventory.*;
import java.util.function.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import com.tke.boss.controller.*;
import org.bukkit.event.entity.*;
import com.tke.boss.models.*;
import org.bukkit.event.world.*;
import org.bukkit.entity.*;

public class BossListener implements Listener
{
    private BossArena instance;
    
    public BossListener() {
        this.instance = BossArena.getInstance();
    }
    
    @EventHandler
    void evento(final BlockPlaceEvent e) {
        final Player p = e.getPlayer();
        if (p.getItemInHand() == null || p.getItemInHand().getType() != Material.SKULL_ITEM || p.getItemInHand().getItemMeta().getDisplayName() == null) {
            return;
        }
        final BossType boss = this.getInstance().getBossTypeController().getBossType(p.getItemInHand().getItemMeta().getDisplayName());
        if (boss == null) {
            return;
        }
        e.setCancelled(true);
        final Location loc = e.getBlockPlaced().getLocation();
        if (!Util.check(p, loc)) {
            p.sendMessage("§c\u00c1rea negada.");
            return;
        }
        new SchematicController().paste(loc);
        BossArena.getInstance().getBossController().spawn(loc.add(0.5, 0.0, 0.5), boss);
        final ItemStack item = p.getItemInHand();
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        }
        else {
            p.setItemInHand(new ItemStack(0));
        }
        p.updateInventory();
        boss.getMessage().forEach(p::sendMessage);
    }
    
    @EventHandler
    void evento(final InventoryCloseEvent e) {
        final BossType boss = this.getInstance().getBossTypeController().getBossType(e.getInventory().getTitle());
        if (boss == null) {
            return;
        }
        boss.setItems(e.getInventory().getContents());
        this.getInstance().getBossTypeController().getConfig().set("Bosses." + boss.getId() + ".items", BossTypeController.toBase64(e.getInventory().getContents()));
        this.getInstance().getBossTypeController().getConfig().saveConfig();
        e.getPlayer().sendMessage("§aItens atualizados.");
    }
    
    @EventHandler
    void evento(final EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof ArmorStand) {
            final Player p = (Player)e.getDamager();
            final ArmorStand stand = (ArmorStand)e.getEntity();
            final Boss boss = this.getInstance().getBossController().getBoss(stand);
            if (boss == null) {
                return;
            }
            boss.damage(p, e.getDamage());
        }
    }
    
    @EventHandler
    void evento(final ChunkUnloadEvent e) {
        if (e.getChunk().getEntities().length > 0) {
            for (final Entity ents : e.getChunk().getEntities()) {
                if (ents instanceof ArmorStand) {
                    final ArmorStand stand = (ArmorStand)ents;
                    if (Boss.bossIndex.containsKey(stand)) {
                        e.setCancelled(true);
                        e.getChunk().load();
                        e.getChunk().load(true);
                    }
                }
            }
        }
    }
    
    public BossArena getInstance() {
        return this.instance;
    }
}
