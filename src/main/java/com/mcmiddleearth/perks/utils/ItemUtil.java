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
package com.mcmiddleearth.perks.utils;

import static com.mcmiddleearth.perks.MCMEPerks.itemType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class ItemUtil {

	public static void giveItem(Player p, String perk) {
		p.getInventory().addItem(new ItemStack(itemPerk(p, perk)));
	}

	public static ItemStack itemPerk(Player p, String perk) {
		String itemName = "boop!";
		String owner = p.getName();
		List<String> lore = new ArrayList<String>();
                Enchantment enchant = Enchantment.OXYGEN;

		Material m = Material.getMaterial(itemType);

		if (perk.equals("jump")) {
			itemName = ChatColor.DARK_BLUE + "Jump Perk";
			lore.add(ChatColor.DARK_PURPLE + owner);
		}
		if (perk.equals("sprint")) {
			itemName = ChatColor.DARK_GREEN + "Sprint Perk";
			lore.add(ChatColor.DARK_PURPLE + owner);
		}
		if (perk.equals("slowmo")) {
			itemName = ChatColor.DARK_AQUA + "SlowMo Perk";
			lore.add(ChatColor.DARK_PURPLE + owner);
		}
		if (perk.equals("dizzy")) {
			itemName = ChatColor.DARK_RED + "Dizzy Perk";
			lore.add(ChatColor.DARK_PURPLE + owner);
		}
		if (perk.equals("hookshot")) {
            m = Material.FISHING_ROD;
            itemName = ChatColor.GOLD + "Grappling Hook Perk";
            lore.add(ChatColor.DARK_PURPLE + owner);
            enchant = Enchantment.DURABILITY;
        }
		if (perk.equals("jockey")) {
			itemName = ChatColor.YELLOW+ "Jockey Perk";
			lore.add(ChatColor.DARK_PURPLE + owner);
		}
		ItemStack it = new ItemStack(m);
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(itemName);
		meta.setLore(lore);
		it.setItemMeta(meta);
		it.addUnsafeEnchantment(enchant, 10);
		it.setAmount(1);
		return it;
	}

	@SuppressWarnings("unused")
	private static int getRandom(int lower, int upper) {
		Random random = new Random();
		return random.nextInt((upper - lower) + 1) + lower;
	}

}
