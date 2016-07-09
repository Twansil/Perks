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
package com.mcmiddleearth.perks.listeners;

import static com.mcmiddleearth.perks.MCMEPerks.scd;
import static com.mcmiddleearth.perks.MCMEPerks.toggle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class ItemListener implements Listener {

	@EventHandler
	private void itemClick(PlayerInteractEvent event) {
		Player p = event.getPlayer();
Logger.getGlobal().info("1");
		if (event.hasItem() && (event.getAction().equals(Action.RIGHT_CLICK_AIR) 
				|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
				&& p.getItemInHand()!=null
                                && p.getItemInHand().hasItemMeta()
                                && p.getItemInHand().getItemMeta().hasDisplayName() 
				&& p.getItemInHand().getItemMeta().hasLore() 
				&& toggle) {
Logger.getGlobal().info("2");
			ItemMeta itm = p.getItemInHand().getItemMeta();
			if (itm.getLore().toString().contains(p.getName())) {
				
Logger.getGlobal().info("3");
				String perk = itm.getDisplayName();
				String perknode = ChatColor.stripColor(perk
						.replace(" Perk", "").toLowerCase());
				
Logger.getGlobal().info("4");
				if (p.hasPermission("perks.item." + perknode)) {
					if(perknode.equals("jockey")){
						JockeyListener.jockey(event);
						event.setCancelled(true);
					}
Logger.getGlobal().info("5");
					if(perknode.equals("grappling hook")){
						event.setCancelled(false);
					} else {
						event.setCancelled(true);
						PotionEffectType effect = getEffect(perk).getType();
						Collection<PotionEffect> activeEffects = p.getActivePotionEffects();
						if (activeEffects.toString().contains(effect.getName())) {
							p.removePotionEffect(effect);
							if(effect.getName().contains("SLOW") && activeEffects.toString().contains("SLOW")){
								p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
							}
						} else {
							p.addPotionEffects(addPotionEffect(p, perk));
						}
					}
				}
			}else {
                                if (p.getItemInHand().getItemMeta().getDisplayName().contains(" Perk")) {
                                        p.sendMessage(scd + "Sorry, that is not your perk item!");
                                        p.getInventory().remove(p.getItemInHand());
                                }
			}
		} else {
			if (!toggle) {
				p.sendMessage(scd + "Sorry, perks have been disabled!");
			}
		}
	}

	private Collection<PotionEffect> addPotionEffect(Player p, String name) {
		Collection<PotionEffect> PES = new ArrayList<PotionEffect>();
		PotionEffect ptn = new PotionEffect(PotionEffectType.HEAL, 0, 0);
		PotionEffect ptn1 = new PotionEffect(PotionEffectType.HEAL, 0, 0);
		ptn = getEffect(name);
		if (ptn.getType().equals(PotionEffectType.SLOW)) {
			ptn1 = new PotionEffect(PotionEffectType.SLOW_DIGGING, 300, 10);
			PES.add(ptn1);
		}
		PES.add(ptn);
		return PES;
	}

	private PotionEffect getEffect(String name) {
		PotionEffect ptn = new PotionEffect(PotionEffectType.HEAL, 0, 0);
		if (name.contains("Jump")) {
			ptn = new PotionEffect(PotionEffectType.JUMP, 600, 4);
		}
		if (name.contains("Sprint")) {
			ptn = new PotionEffect(PotionEffectType.SPEED, 600, 2);
		}
		if (name.contains("SlowMo")) {
			ptn = new PotionEffect(PotionEffectType.SLOW, 300, 3);
		}
		if (name.contains("Dizzy")) {
			ptn = new PotionEffect(PotionEffectType.CONFUSION, 300, 5);
		}
		return ptn;
	}

}
