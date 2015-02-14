/*
 * This file is part of Perks.
 * 
 * Perks is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Perks is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Perks.  If not, see <http://www.gnu.org/licenses/>.
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
