package com.pablo67340.anticam;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Anticam extends JavaPlugin {

	public ChestListener chestlistener = new ChestListener(this);

	public void onDisable(){
		Bukkit.getScheduler().cancelTasks(this);
	}
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(chestlistener, this);
	}
}
