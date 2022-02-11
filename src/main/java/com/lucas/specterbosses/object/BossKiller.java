package com.lucas.specterbosses.object;

public class BossKiller {
	
	private String name;
	private int life;
	
	public BossKiller(String name, int life) {
		this.name = name;
		this.life = life;
	}
	
	public int getLife() {
		return this.life;
	}
	
	public String getItem() {
		return this.name;
	}
	
}
