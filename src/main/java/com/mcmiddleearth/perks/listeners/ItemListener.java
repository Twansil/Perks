package com.mcmiddleearth.perks.listeners;

import static com.mcmiddleearth.perks.MCMEPerks.scd;
import static com.mcmiddleearth.perks.MCMEPerks.toggle;

import java.util.ArrayList;
import java.util.Collection;

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
		if (event.hasItem() && (event.getAction().equals(Action.RIGHT_CLICK_AIR) 
				|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
				&& p.getItemInHand().getItemMeta().hasDisplayName() 
				&& p.getItemInHand().getItemMeta().hasLore() 
				&& toggle) {
			ItemMeta itm = p.getItemInHand().getItemMeta();
			if (itm.getLore().toString().contains(p.getName())) {
				
				String perk = itm.getDisplayName();
				String perknode = ChatColor.stripColor(perk
						.replace(" Perk", "").toLowerCase());
				
				if (p.hasPermission("perks.item." + perknode)) {
					if(perknode.equals("jockey")){
						JockeyListener.jockey(event);
						event.setCancelled(true);
					}
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
			} else {
				p.sendMessage(scd + "Sorry, that is not your perk item!");
				p.getInventory().remove(p.getItemInHand());
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
