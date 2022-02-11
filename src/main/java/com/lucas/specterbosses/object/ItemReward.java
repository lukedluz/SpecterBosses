package com.lucas.specterbosses.object;

import org.bukkit.inventory.ItemStack;

public class ItemReward {
	
	ItemStack item;
	int porcentagem;
	String cmd;
	
	public ItemReward(ItemStack item, int porcentagem) {
		this.item = item;
		this.porcentagem = porcentagem;
	}
	
	public int getPorcentagem() {
		return this.porcentagem;
	}
	
	public ItemStack getItem() {
		return this.item;
	}
	
	public void setCMD(String cmds) {
		this.cmd = cmds;
	}
	
	public String getCMD() {
		return this.cmd;
	}

}
