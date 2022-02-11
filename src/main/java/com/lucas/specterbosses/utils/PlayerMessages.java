package com.lucas.specterbosses.utils;

import com.lucas.specterbosses.Main;

public class PlayerMessages {
	
	public static String BOSSGIVECOMMAND;
	public static String BOSSKILLERCOMMAND;
	public static String BOSSRELOADCOMMAND;
	public static String BOSSGAVE;
	public static String BOSSKILLERGAVE;
	public static String BOSSLIFE;
	public static String BOSSNOPERMISSION;
	public static String BOSSRELOADED;
	public static String BOSSDOESNTEXISTS;
	public static String BOSSDOESNTEXISTSs;
	public static int NEE;
	public static String WAIT;
	public static String ONLY;
	
	public static void loadMessage() {
		BOSSGIVECOMMAND = Main.get().getConfig().getString("Messages.BossGiveCommand").replace("&", "§");
		BOSSKILLERCOMMAND = Main.get().getConfig().getString("Messages.BossKillerCommand").replace("&", "§");
		BOSSRELOADCOMMAND = Main.get().getConfig().getString("Messages.BossReloadCommand").replace("&", "§");
		BOSSGAVE = Main.get().getConfig().getString("Messages.BossGave").replace("&", "§");
		BOSSKILLERGAVE = Main.get().getConfig().getString("Messages.BossKillerGave").replace("&", "§");
		BOSSLIFE = Main.get().getConfig().getString("ActionBar").replace("&", "§");
		BOSSNOPERMISSION = Main.get().getConfig().getString("Messages.BossNoPermission").replace("&", "§");
		BOSSRELOADED = Main.get().getConfig().getString("Messages.BossReloaded").replace("&", "§");
		BOSSDOESNTEXISTS = Main.get().getConfig().getString("Messages.BossDoesntExists").replace("&", "§");
		BOSSDOESNTEXISTSs = Main.get().getConfig().getString("Messages.BossMobNear").replace("&", "§");
		WAIT = Main.get().getConfig().getString("Messages.Wait").replace("&", "§");
		NEE = Main.get().getConfig().getInt("Distance");
		ONLY = Main.get().getConfig().getString("Messages.OnlyUsingKiller").replace("&", "§");
	}

}
