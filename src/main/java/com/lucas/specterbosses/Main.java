package com.lucas.specterbosses;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lucas.specterbosses.commands.BossCommand;
import com.lucas.specterbosses.commands.GiveBossCommand;
import com.lucas.specterbosses.commands.GiveBossKillerCommand;
import com.lucas.specterbosses.listeners.DamageEvent;
import com.lucas.specterbosses.listeners.DeathEvent;
import com.lucas.specterbosses.listeners.InteractEvent;
import com.lucas.specterbosses.object.Boss;
import com.lucas.specterbosses.object.BossKiller;
import com.lucas.specterbosses.object.ItemReward;
import com.lucas.specterbosses.utils.PlayerMessages;

public class Main extends JavaPlugin{
	
	private static Main plugin;

	private static ArrayList<Boss> bosses = new ArrayList<>();
	private static ArrayList<BossKiller> killerboss = new ArrayList<>();
	
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§7==========================");
		Bukkit.getConsoleSender().sendMessage("§7| §bSpecterBosses          §7|");
		Bukkit.getConsoleSender().sendMessage("§7| §bVersão 1.0             §7|");
		Bukkit.getConsoleSender().sendMessage("§7| §fStatus: §aLigado       §7|");
		Bukkit.getConsoleSender().sendMessage("§7==========================");
		Bukkit.getConsoleSender().sendMessage("");

		plugin = this;
		
		saveDefaultConfig();
		load();
		PlayerMessages.loadMessage();
		getCommand("boss").setExecutor(new BossCommand());
		getCommand("giveboss").setExecutor(new GiveBossCommand());
		getCommand("givebosskiller").setExecutor(new GiveBossKillerCommand());
		Bukkit.getPluginManager().registerEvents(new InteractEvent(), this);
		Bukkit.getPluginManager().registerEvents(new DamageEvent(), this);
		Bukkit.getPluginManager().registerEvents(new DeathEvent(), this);
		
	}
	
	public void onDisable() {

	
	}
	
	public static Main get() {
		return plugin;
	}

	public ArrayList<Boss> getBosses() {
		return bosses;
	}
	
	public ArrayList<BossKiller> getBossKiller(){ 
		return killerboss;
	}
	
	@SuppressWarnings("deprecation")
	public void load() {
		
		int bossedw = 0;
		int killer = 0;

		for (String key : getConfig().getConfigurationSection("Bosses").getKeys(false)) {
			
			ItemStack itesm = new ItemStack(Material.getMaterial(getConfig().getInt("Bosses." + key + ".Icon.ID"))
					,1
							,(short)getConfig().getInt("Bosses." + key + ".Icon.Data"));
			ItemMeta itemsmeta = itesm.getItemMeta();
			itemsmeta.setDisplayName(getConfig().getString("Bosses." + key + ".Icon.Name").replace("&", "§"));
			ArrayList<String> lo = new ArrayList<>();
			for (String st : getConfig().getStringList("Bosses." + key + ".Icon.Lore")) {
				lo.add(st.replace("&", "§"));
			}
			itemsmeta.setLore(lo);
			itesm.setItemMeta(itemsmeta);
			
			Boss boss = new Boss(EntityType.valueOf(getConfig().getString("Bosses." + key + ".Boss.Mob_Type")), getConfig().getString("Bosses." + key + ".Boss_Name"), getConfig().getString("Bosses." + key + ".Boss.Mob_Custom_Name"), getConfig().getInt("Bosses." + key + ".Boss.Mob_Life"),  getConfig().getInt("Bosses." + key + ".Boss.Mob_Hit"), itesm);
			
			List<PotionEffect> potion = new ArrayList<>();
			
			for (String s : getConfig().getStringList("Bosses." + key + ".Boss.Side_Effects")) {
				Integer id = Integer.valueOf(s.split(":")[0]);
				Integer duration = Integer.valueOf(s.split(":")[1]);
				Integer d = Integer.valueOf(s.split(":")[2]);
				potion.add(new PotionEffect(PotionEffectType.getById(id), duration*20, d));
			}
			
			boss.setPotionEffect(potion);
			
			ArrayList<ItemReward> items = new ArrayList<>();
			List<String> cmds = getConfig().getStringList("Bosses." + key + ".Commands");
			
			for (String cmd : cmds) {
				
				ItemReward it = new ItemReward(null, Integer.valueOf(cmd.split(";")[1]));
				it.setCMD(cmd.split(";")[0]);
				items.add(it);

			}
			
			for (String rewards : getConfig().getConfigurationSection("Bosses." + key + ".Rewards").getKeys(false)) {
				
				ItemStack item = new ItemStack(Material.getMaterial(getConfig().getInt("Bosses." + key + ".Rewards." + rewards + ".ID"))
						,getConfig().getInt("Bosses." + key + ".Rewards." + rewards + ".Amount")
								,(short)getConfig().getInt("Bosses." + key + ".Rewards." + rewards + ".Data"));
				ItemMeta itemmeta = item.getItemMeta();
				if (getConfig().get("Bosses." + key + ".Rewards." + rewards + ".Name") != null) {
					itemmeta.setDisplayName(getConfig().getString("Bosses." + key + ".Rewards." + rewards + ".Name").replace("&", "§"));
				}
				if (getConfig().get("Bosses." + key + ".Rewards." + rewards + ".Lore") != null) {
					ArrayList<String> los = new ArrayList<>();
					for (String st : getConfig().getStringList("Bosses." + key + ".Rewards." + rewards + ".Lore")) {
						los.add(st.replace("&", "§"));
					}
					itemmeta.setLore(los);
				}
				item.setItemMeta(itemmeta);
				for (String ench1 : getConfig().getStringList("Bosses." + key + ".Rewards." + rewards + ".Enchantments")) {
					String[] split = ench1.split(":");
					try {
						item.addUnsafeEnchantment(Enchantment.getById(Integer.valueOf(split[0])), Integer.valueOf(split[1]));
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§cEncantamento de ID = " + split[0] + " e level = " + split[1] + " falhou");
					}
				}
				ItemReward i = new ItemReward(item, getConfig().getInt("Bosses." + key + ".Rewards." + rewards + ".Chance"));
				
				items.add(i);
			}
			
			bossedw += 1;
			
			boss.setRewards(items);
			bosses.add(boss);
			
		}
		
		for (String key : getConfig().getConfigurationSection("KillerItems").getKeys(false)) {
			
			killerboss.add(new BossKiller(getConfig().getString("KillerItems." + key + ".Name").replace("&", "§"), getConfig().getInt("KillerItems." + key + ".Life")));
			killer += 1;
			
		}
		
		
		Bukkit.getConsoleSender().sendMessage("[SpecterBosses] Foram carregados, " + bossedw + " bosses no total.");
		Bukkit.getConsoleSender().sendMessage("[SpecterBosses] Foram carregados, " + killer + " matadoras no total.");
		
	}
	
	public String getAllBosses() {
		String sb = "";
		for (String key : getConfig().getConfigurationSection("Bosses").getKeys(false)) {
			sb += getConfig().getString("Bosses." + key + ".Boss_Name") + ", ";
		}
		return sb;
	}
	
}
