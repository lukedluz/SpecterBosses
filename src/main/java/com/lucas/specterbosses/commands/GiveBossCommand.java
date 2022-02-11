package com.lucas.specterbosses.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lucas.specterbosses.utils.PlayerMessages;
import com.lucas.specterbosses.utils.Utilities;

public class GiveBossCommand implements CommandExecutor{
	
	PlayerMessages msg = new PlayerMessages();
	
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
		if (!sender.hasPermission("boss.admin")) {
			sender.sendMessage(PlayerMessages.BOSSNOPERMISSION);
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(PlayerMessages.BOSSGIVECOMMAND);
			return true;
		}
		if (args.length == 1) {
			sender.sendMessage(PlayerMessages.BOSSGIVECOMMAND);
			return true;
		}
		Player p = Bukkit.getPlayer(args[0]);
		try {
			Integer.parseInt(args[2]);
		} catch (Exception e) {
			sender.sendMessage(PlayerMessages.BOSSGIVECOMMAND);
			return true;
		}
		Utilities.addItemByBossName(sender, args[1], p, Integer.valueOf(args[2]));
		return false;
	}
}
