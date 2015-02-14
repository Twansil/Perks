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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class HorseUtil {

	public static boolean spawn = false;

	public static void giveHorse(Player p) {
		Location l = p.getLocation();
		World w = p.getWorld();

		spawn = true;
		Horse horsey = w.spawn(l, Horse.class);
		horsey.setAdult();
		horsey.setVariant(Horse.Variant.HORSE);
		horsey.setColor(randomColor());
		horsey.setStyle(randomStyle());
		horsey.setTamed(true);
		horsey.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		horsey.setOwner(p);
		horsey.setCustomName(ChatColor.DARK_AQUA + p.getName()
				+ "'s Sweet Ride!");

		horsey.setPassenger(p);
		spawn = false;
	}

	public static Variant randomVariant() {
		Variant variant = Horse.Variant.HORSE;
		List<Variant> variants = new ArrayList<Variant>();
		for (Variant var : Horse.Variant.values()) {
			variants.add(var);
		}
		Collections.shuffle(variants);
		variant = variants.get(0);
		return variant;
	}

	public static Color randomColor() {
		List<Color> colors = new ArrayList<Color>();
		for (Color col : Horse.Color.values()) {
			colors.add(col);
		}
		Collections.shuffle(colors);
		return colors.get(0);
	}

	public static Style randomStyle() {
		List<Style> styles = new ArrayList<Style>();
		for (Style st : Horse.Style.values()) {
			styles.add(st);
		}
		Collections.shuffle(styles);
		return styles.get(0);
	}

}
