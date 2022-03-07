package com.tke.boss.command;

import com.tke.boss.*;
import org.bukkit.command.*;
import org.bukkit.*;
import com.tke.boss.api.*;
import org.bukkit.entity.*;
import com.tke.boss.models.*;
import org.bukkit.inventory.*;

public class Comando implements CommandExecutor
{
    private BossArena instance;
    
    public Comando(final BossArena instance) {
        this.instance = instance;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String lb, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("giveboss")) {
            if (args.length < 2) {
                sender.sendMessage("§cUse: §f/giveboss §7<player> <boss> <quantidade>");
                return true;
            }
            final Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                sender.sendMessage("§cPlayer n\u00e3o est\u00e1 online.");
                return true;
            }
            final BossType bossType = this.getInstance().getBossTypeController().getBossType(args[1]);
            if (bossType == null) {
                sender.sendMessage("§cEsse boss n\u00e3o existe.");
                return true;
            }
            final int quantidade = Integer.parseInt(args[2]);
            final ItemStack item = new ItemBuilder().skull(bossType.getTexture()).name(bossType.getDisplayName()).amount(quantidade).getItem();
            player.getInventory().addItem(new ItemStack[] { item });
        }
        if (cmd.getName().equalsIgnoreCase("setitems")) {
            if (!(sender instanceof Player)) {
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0) {
                sender.sendMessage("§cUse: §f/giveboss §7<boss>");
                return true;
            }
            final BossType bossType = this.getInstance().getBossTypeController().getBossType(args[0]);
            if (bossType == null) {
                sender.sendMessage("§cEsse boss n\u00e3o existe.");
                return true;
            }
            final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, bossType.getDisplayName());
            p.openInventory(inv);
        }
        return false;
    }
    
    public BossArena getInstance() {
        return this.instance;
    }
}
