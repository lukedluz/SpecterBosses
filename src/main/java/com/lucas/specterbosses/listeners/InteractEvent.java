package com.lucas.specterbosses.listeners;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.lucas.specterbosses.Main;
import com.lucas.specterbosses.object.Boss;
import com.lucas.specterbosses.utils.PlayerMessages;
import com.lucas.specterbosses.utils.Utilities;

public class InteractEvent implements Listener {

	Utilities utilities = new Utilities();
	Main plugin = Main.get();
	HashMap<Player, Long> cooldown = new HashMap<>();

	@EventHandler(priority = EventPriority.LOW)
	public void onClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (!e.getClickedBlock().getLocation().getWorld().getName().equalsIgnoreCase("Terrenos"))
			return;
		ItemStack item = player.getItemInHand();
		if (item == null || item.getType() == Material.AIR) {
			return;
		}
		for (Boss bosses : plugin.getBosses()) {
			if (item.isSimilar(bosses.getItem())) {

				if (hasMobs(player)) {
					e.setCancelled(true);
					player.sendMessage(PlayerMessages.BOSSDOESNTEXISTSs);
					return;
				}
				if (cooldown.containsKey(player) && cooldown.get(player) > System.currentTimeMillis()) {
					e.setCancelled(true);
					player.sendMessage(PlayerMessages.WAIT);
					return;
				}

				e.setCancelled(true);
				if (Main.get().getConfig().getBoolean("CooldownAtivado") == true) {
					cooldown.put(player, System.currentTimeMillis()
							+ TimeUnit.SECONDS.toMillis(plugin.getConfig().getInt("Cooldown")));
				}

				Location loc = e.getClickedBlock().getLocation();
				loc.setY(loc.getY() + 1);
				Utilities.spawnEntity(bosses, loc);
				if (player.getItemInHand().getAmount() == 1) {
					player.getInventory().remove(player.getItemInHand());
				} else {
					player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
				}
				player.updateInventory();
				break;
			}
		}
	}

	public boolean hasMobs(Player p) {
		boolean bo = false;
		for (Entity s : p.getNearbyEntities(PlayerMessages.NEE, PlayerMessages.NEE, PlayerMessages.NEE)) {
			if (s instanceof Monster) {
				bo = true;
				break;
			}
		}
		return bo;
	}

}
