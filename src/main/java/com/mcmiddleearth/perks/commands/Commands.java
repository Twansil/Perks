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

import static com.mcmiddleearth.perks.MCMEPerks.err;
import static com.mcmiddleearth.perks.MCMEPerks.perks;
import static com.mcmiddleearth.perks.MCMEPerks.prim;
import static com.mcmiddleearth.perks.MCMEPerks.scd;
import static com.mcmiddleearth.perks.MCMEPerks.toggle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mcmiddleearth.perks.MCMEPerks;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String c, String[] a) {
		if (!(cs instanceof Player)) {
			cs.sendMessage(err + "This command can only be run by a player.");
			return true;
		} else {
			Player p = (Player) cs;
			if (c.equalsIgnoreCase("mount") && a.length == 0) {
				if (toggle) {
					if (cs.hasPermission("perks.user")) {
						CommandMethods.giveHorse(p);
						return true;
					} else {
						cs.sendMessage(scd
								+ "Sorry, you don't have permissions for that!");
						return true;
					}
				} else {
					cs.sendMessage(scd + "Sorry, perks have been disabled!");
					return true;
				}
			}
			if (c.equalsIgnoreCase("perk")) {
				if (toggle) {
					if (a.length == 1) {
						if (cs.hasPermission("perks.user")) {
							CommandMethods.giveItem(p, a[0]);
							return true;
						} else {
							cs.sendMessage(scd
									+ "Sorry, you don't have permissions for that!");
							return true;
						}
					} else {
						cs.sendMessage(prim + "/perk " + perks.toString());
						return true;
					}
				} else {
					cs.sendMessage(scd + "Sorry, perks have been disabled!");
					return true;
				}
			}
			if (c.equalsIgnoreCase("walk")) {
				if (toggle) {
					if (a.length == 0) {
						CommandMethods.playerWalk(p);
						return true;
					}
				}
			}
			if (c.equalsIgnoreCase("perkstoggle")) {
				if (cs.hasPermission("perks.admin")) {
					if (toggle) {
						toggle = false;
						CommandMethods.saveToggle();
						cs.sendMessage(scd + "Perks have been toggled " + prim
								+ "off!");
						MCMEPerks.clearHorses();
						MCMEPerks.unJockeyAll();
						return true;
					} else {
						toggle = true;
						CommandMethods.saveToggle();
						cs.sendMessage(scd + "Perks have been toggled " + prim
								+ "on!");
						return true;
					}
				}
			}
		}
		return false;
	}
}
