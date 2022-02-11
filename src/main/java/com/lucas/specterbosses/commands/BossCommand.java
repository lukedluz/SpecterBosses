package com.lucas.specterbosses.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.lucas.specterbosses.Main;
import com.lucas.specterbosses.utils.PlayerMessages;

public class BossCommand implements CommandExecutor{
	
	PlayerMessages msg = new PlayerMessages();
	
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
		if (!sender.hasPermission("boss.admin")) {
			sender.sendMessage(PlayerMessages.BOSSNOPERMISSION);
			return true;
		}
		if (args.length != 1) {
			sender.sendMessage(PlayerMessages.BOSSRELOADCOMMAND);
			return true;
		}
		if (args[0].equalsIgnoreCase("reload")) {
			sender.sendMessage(PlayerMessages.BOSSRELOADED);
			Main.get().reloadConfig();
			Main.get().load();
			return true;
		}
		sender.sendMessage(PlayerMessages.BOSSRELOADCOMMAND);
		return false;
	}

}
