package com.lucas.specterbosses.object;

import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Boss {
	
	private EntityType type;
	private String name;
	private String custom_name;
	private int life;
	private int hit;
	private List<ItemReward> rewards;
	private List<PotionEffect> potionEffect;
	
	private ItemStack item;
	
	public Boss(EntityType type, String name, String custom_name, int life, int hit, ItemStack item) {
		this.type = type;
		this.name = name;
		this.custom_name = custom_name;
		this.life = life;
		this.item = item;
		this.hit = hit;
	}
	
	public int getHit() {
		return hit;
	}
	
	public int getLife() {
		return life;
	}
	
	public String getName() {
		return name;
	}

	public String getCustomName() {
		return custom_name;
	}
	
	public EntityType getType() {
		return type;
	}
	
	public ItemStack getItem() {
		return item;
	}

	public List<ItemReward> getRewards() {
		return rewards;
	}

	public void setRewards(List<ItemReward> rewards) {
		this.rewards = rewards;
	}

	public List<PotionEffect> getPotionEffect() {
		return potionEffect;
	}

	public void setPotionEffect(List<PotionEffect> potionEffect) {
		this.potionEffect = potionEffect;
	}

}
