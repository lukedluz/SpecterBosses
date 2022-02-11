package com.lucas.specterbosses.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lucas.specterbosses.Main;
import com.lucas.specterbosses.utils.ItemBuilder;
import com.lucas.specterbosses.utils.PlayerMessages;

public class GiveBossKillerCommand implements CommandExecutor{
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
		if (!sender.hasPermission("boss.admin")) {
			sender.sendMessage(PlayerMessages.BOSSNOPERMISSION);
			return true;
		}
		if (args.length != 2) {
			sender.sendMessage(PlayerMessages.BOSSKILLERCOMMAND);
			return true;
		}
		Player p = Bukkit.getPlayer(args[0]);
		for (String key : Main.get().getConfig().getConfigurationSection("KillerItems").getKeys(false)) {
			if (args[1].equalsIgnoreCase(key)) {

				ItemBuilder item = new ItemBuilder(Material.getMaterial(Main.get().getConfig().getInt("KillerItems." + key + ".ID")));
				item.durability(Main.get().getConfig().getInt("KillerItems." + key + ".Data"));
				item.name(Main.get().getConfig().getString("KillerItems." + key + ".Name").replace("&", "§"));
				ArrayList<String> l = new ArrayList<>();
				for (String s : Main.get().getConfig().getStringList("KillerItems." + key + ".Lore")) {
					l.add(s.replace("&", "§"));
				}
				item.listLore(l);
				if (Main.get().getConfig().getBoolean("KillerItems." + key + ".Glow")) {
					item.enchant(Enchantment.SILK_TOUCH, 1);
					item.removeAttributes();
				}
				
				ItemStack i = item.build();
						
				
				if (Main.get().getConfig().getBoolean("KillerItems." + key + ".Durability")) {
					i.getItemMeta().spigot().setUnbreakable(true);
				}
				
				p.getInventory().addItem(item.build());
				sender.sendMessage(PlayerMessages.BOSSKILLERGAVE.replace("@bosskiller_name", Main.get().getConfig().getString("KillerItems." + key + ".Name").replace("&", "§")).replace("@player", args[0]));
				
				break;
			}
		}
		return false;
	}

}
