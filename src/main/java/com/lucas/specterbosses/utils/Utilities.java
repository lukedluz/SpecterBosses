package com.lucas.specterbosses.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lucas.specterbosses.Main;
import com.lucas.specterbosses.object.Boss;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Utilities {
	
	private static Main plugin = Main.get();
	
	public static void spawnEntity(Boss bosses, Location loc) {
		LivingEntity entity = (LivingEntity) Bukkit.getWorld(loc.getWorld().getName()).spawnEntity(loc, bosses.getType());
		entity.setCustomName(bosses.getCustomName().replace("&", "§"));
		entity.setMaxHealth(bosses.getLife());
		entity.setHealth(bosses.getLife());
		
		String a = Bukkit.getServer().getClass().getPackage().getName();
		String version = a.substring(a.lastIndexOf('.') + 1);

		if ((version.contains("1_8"))) {
			net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
			NBTTagCompound tag = nmsEntity.getNBTTag();
			if (tag == null) {
				tag = new NBTTagCompound();
			}
			nmsEntity.c(tag);
			tag.setInt("NoAI", 1);
			nmsEntity.f(tag);
		}
	}

	public static void addItemByBossName(CommandSender sender, String name, Player p, int amount) {
		for (Boss bosses : plugin.getBosses()) {
			if (name.equalsIgnoreCase(bosses.getName())) {
				ItemStack item = bosses.getItem();
				item.setAmount(amount);
				p.getInventory().addItem(item);
				sender.sendMessage(PlayerMessages.BOSSGAVE.replace("@boss_amount", String.valueOf(amount)).replace("@player", p.getName()).replace("@boss", name));
				break;
			}
		}
	}
	
}
