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
