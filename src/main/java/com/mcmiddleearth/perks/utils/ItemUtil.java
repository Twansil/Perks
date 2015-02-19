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
                    itemName = ChatColor.RED + "Grappling Hook Perk";
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
