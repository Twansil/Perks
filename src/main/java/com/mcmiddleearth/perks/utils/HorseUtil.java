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
