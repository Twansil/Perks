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
package com.mcmiddleearth.perks.commands;

import static com.mcmiddleearth.perks.MCMEPerks.perks;
import static com.mcmiddleearth.perks.MCMEPerks.prim;
import static com.mcmiddleearth.perks.MCMEPerks.scd;
import static com.mcmiddleearth.perks.MCMEPerks.toggle;
import static com.mcmiddleearth.perks.MCMEPerks.walkSpeed;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.mcmiddleearth.perks.MCMEPerks;
import com.mcmiddleearth.perks.utils.HorseUtil;
import com.mcmiddleearth.perks.utils.ItemUtil;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class CommandMethods {

	public static void giveHorse(Player p) {
		if (p.hasPermission("perks.mount")) {
			if (p.isInsideVehicle()) {
				p.sendMessage(scd + "You are already on a horse!");
				return;
			} else {
				HorseUtil.giveHorse(p);
				return;
			}
		} else {
			p.sendMessage(scd + "Sorry you haven't unlocked this perk!");
			return;
		}
	}

	public static int clearHorses(Player p) {
		World w = p.getWorld();
		int i = 0;
		for (Entity e : w.getEntities()) {
			if (e.getType().equals(EntityType.HORSE)) {
				e.remove();
				i++;
			}
		}
		return i;
	}

	public static void giveItem(Player p, String perk) {
		String str = perk.toLowerCase();
		if (perks.contains(str)) {
			if (p.hasPermission("perks.item." + str)) {
				ItemUtil.giveItem(p, str);
				return;
			} else {
				p.sendMessage(scd + "Sorry, you haven't unlocked that perk!");
				return;
			}
		} else {
			p.sendMessage(prim + "/perk " + perks.toString());
			return;
		}
	}
	
	public static void playerWalk(Player p){
		if(p.hasPermission("perks.walk")){
			if(p.getWalkSpeed()!=(float) 0.2){
				p.setWalkSpeed((float) 0.2);
				p.sendMessage(scd + "Walk-speed disabled!");
				return;
			} else {
				p.setWalkSpeed(walkSpeed);
				p.sendMessage(prim + "Walk-speed enabled!");
				return;
			}
		} else {
			p.sendMessage(scd + "Sorry you don't have permission to use that!");
			return;
		}
	}

	public static void saveToggle() {
		MCMEPerks.inst().getConfig().set("Settings.Enabled", toggle);
		MCMEPerks.inst().saveConfig();
	}
}
