/*
 * Copyright (c) 2015 dags_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * 
 */
package com.mcmiddleearth.perks;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcmiddleearth.perks.commands.Commands;
import com.mcmiddleearth.perks.listeners.GrapplingHookListener;
import com.mcmiddleearth.perks.listeners.HorseListener;
import com.mcmiddleearth.perks.listeners.ItemListener;
import com.mcmiddleearth.perks.listeners.TagListener;
import com.mcmiddleearth.perks.listeners.JockeyListener;
import org.bukkit.Material;
;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class MCMEPerks extends JavaPlugin {

	private static Plugin plugin;
	public static boolean toggle;	
	public static int itemType;
	public static Float walkSpeedConf;
	public static Float walkSpeed;
	public static HashSet<String> perks = new HashSet<String>();
        public static HashSet<Material> perkItems = new HashSet<Material>();
	public static ChatColor prim = ChatColor.DARK_AQUA;
	public static ChatColor scd = ChatColor.GRAY;
	public static ChatColor err = ChatColor.DARK_RED;
	public static ChatColor donarTag;

	public MCMEPerks() {
		super();
		plugin = this;
	}

	public static Plugin inst() {
		return plugin;
	}

	@Override
	public void onEnable() {
		loadDefaults();
		setupConfig();
		registerEvents();
		TagListener.setBoard();
	}

	@Override
	public void onDisable() {
		clearHorses();
		unJockeyAll();
		TagListener.clearBoard();
	}

	private void loadDefaults() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}

	private void setupConfig() {
		for (Object o : this.getConfig().getList("Perks")) {
			perks.add(o.toString().toLowerCase());
		}
		itemType = this.getConfig().getInt("Settings.PerksItemID");
		walkSpeedConf = Float.valueOf(this.getConfig().getString(
				"Settings.WalkSpeed"));
		toggle = this.getConfig().getBoolean("Settings.Enabled");
		tagColor(this.getConfig().getString("Settings.DonarTagColor"));
		setWalkSpeed();
	}

	private void registerEvents() {
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new TagListener(), this);
		pm.registerEvents(new HorseListener(), this);
		pm.registerEvents(new ItemListener(), this);
		pm.registerEvents(new GrapplingHookListener(), this);
		pm.registerEvents(new JockeyListener(), this);
		getCommand("mount").setExecutor(new Commands());
		getCommand("perk").setExecutor(new Commands());
		getCommand("walk").setExecutor(new Commands());
		getCommand("perkstoggle").setExecutor(new Commands());
	}
	
	private static void setWalkSpeed(){
		if(Float.valueOf(walkSpeedConf) < 5.0
				&& Float.valueOf(walkSpeedConf) > 0.0){
			walkSpeed = (float) (walkSpeedConf/5);
		} else {
			walkSpeed = (float) 0.2;
		}
	}
	private static void tagColor(String color){
		try {
			donarTag = ChatColor.valueOf(color.toUpperCase());
		} catch (IllegalArgumentException e) {
			System.out.print("[MCMEPerks] Incorrect color defined in config. Resetting to Yellow!");
			donarTag = ChatColor.YELLOW;
			plugin.getConfig().set("Settings.DonarTagColor", "YELLOW");
			plugin.saveConfig();
		}
	}

	public static void clearHorses() {
		for (World w : Bukkit.getServer().getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e.getType().equals(EntityType.HORSE)) {
					e.remove();
				}
			}
		}
	}
	
	public static void unJockeyAll(){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.isInsideVehicle() && p.getVehicle() instanceof Player){
				p.getVehicle().eject();
			}
		}
	}
}
