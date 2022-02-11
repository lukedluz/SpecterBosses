package com.lucas.specterbosses.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.lucas.specterbosses.Main;
import com.lucas.specterbosses.object.Boss;
import com.lucas.specterbosses.object.BossKiller;
import com.lucas.specterbosses.utils.PlayerMessages;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;

public class DamageEvent implements Listener{

	String s = PlayerMessages.BOSSLIFE;
	String r = PlayerMessages.ONLY;
	Main plugin = Main.get();
	
	@EventHandler(priority=EventPriority.LOW)
	public void on(EntityDamageEvent e) {
		Entity entity = e.getEntity();
		if (entity.getCustomName() == null) {
			return;
		}
		for (Boss W : Main.get().getBosses()) {
			if (entity.getCustomName().equals(W.getCustomName().replace("&", "§"))) {
				if (e.getCause() != DamageCause.ENTITY_ATTACK) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	
	@EventHandler(priority=EventPriority.LOW)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		if (e.getEntity() instanceof Player) {
			return;
		}
		if (!(e.getEntity() instanceof LivingEntity)) {
			return;
		}
		
		Player p = (Player)e.getDamager();
		LivingEntity entity = (LivingEntity) e.getEntity();
		ItemStack item = p.getInventory().getItemInHand();
		
		if (entity.getCustomName() == null) {
			return;
		}
		
		for (Boss W : Main.get().getBosses()) {
			if (entity.getCustomName().equals(W.getCustomName().replace("&", "§"))) {
				
				if (!has(item)) {
					p.sendMessage(r);
					e.setCancelled(true);
					return;
				}
				
				if (getDamageByName(item.getItemMeta().getDisplayName()) == 0) {
					p.sendMessage(r);
					e.setCancelled(true);
					return;
				}
				
				e.setDamage((double)getDamageByName(item.getItemMeta().getDisplayName()));
                for (PotionEffect pt : W.getPotionEffect()) {
                    if (!p.hasPotionEffect(pt.getType())) {
                        p.addPotionEffect(pt);
                    }
                }
                
                if (W.getHit() > 0) {
                	p.damage((double)W.getHit());
                }
                sendActionBar(p, s.replace("%boss_life%", String.valueOf((int)entity.getHealth())).replace("%boss_name%", W.getCustomName()));
                break;
                
			}
		}
	}
	
	public int getDamageByName(String name) {
		int i = 0;
		for (BossKiller st : plugin.getBossKiller()) {
			if (st.getItem().equals(name)) {
				i = st.getLife();
				break;
			}
		}
		return i;
	}
	
	public boolean has(ItemStack item) {
		if (item != null && item.getType() != Material.AIR && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null) {
			return true;
		}else {
			return false;
		}
	}
	
	public void sendActionBar(Player p, String msg) {
		String s = ChatColor.translateAlternateColorCodes('&', msg);
		IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s + "\"}");
		PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
	}
	
}
