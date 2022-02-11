package com.lucas.specterbosses.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.lucas.specterbosses.Main;
import com.lucas.specterbosses.object.Boss;
import com.lucas.specterbosses.object.ItemReward;

public class DeathEvent implements Listener{
	
	public static Boolean percentChance(double chance) {
	    return Math.random() <= chance;
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void entity(EntityDeathEvent e) {
		if (e.getEntity().getCustomName() == null) {
			return;
		}
		for (Boss w : Main.get().getBosses()) {
			if (e.getEntity().getCustomName().equals(w.getCustomName().replace("&", "§"))) {
				for (ItemReward sw : w.getRewards()) {
					Random rand = new Random();
					Player killer = e.getEntity().getKiller();
					int chance = rand.nextInt(100)+1;
					if (chance <= sw.getPorcentagem()) {
						if (sw.getItem() == null) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), sw.getCMD().replace("@player", killer.getName()));
						}else {
							e.getDrops().add(sw.getItem());
						}
					}
				}
				e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
				break;
			}
		}
	}
	
}
