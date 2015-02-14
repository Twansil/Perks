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
package com.mcmiddleearth.perks.listeners;

import static com.mcmiddleearth.perks.MCMEPerks.scd;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import com.mcmiddleearth.perks.utils.HorseUtil;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class HorseListener implements Listener {

	// Checks if user getting on the horse has permission, and that the horse is
	// his. Otherwise removes horse.
	@EventHandler
	public void horseMount(VehicleEnterEvent event) {
		if (event.getEntered() instanceof Player) {
			if (event.getVehicle() instanceof Horse) {
				Player p = (Player) event.getEntered();
				Horse h = (Horse) event.getVehicle();
				if ((!h.getCustomName().contains(p.getName()))
						&& p.hasPermission("perks.mount")) {
					event.setCancelled(true);
					h.remove();
					p.sendMessage(scd + "Sorry, this is not your horse!");
				}
			}
		}
	}

	// Remove horse when rider dismounts
	@EventHandler
	public void horseDismount(VehicleExitEvent event) {
		if (event.getVehicle().getType().equals(EntityType.HORSE)) {
			event.getVehicle().remove();
		}
	}

	// Remove horse if rider dies
	@EventHandler
	public void riderDie(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			if (event.getEntity().isInsideVehicle()) {
				event.getEntity().getVehicle().remove();
			}
		}
	}

	// Remove horse if rider quits
	@EventHandler
	public void riderQuit(PlayerQuitEvent event) {
		if (event.getPlayer().isInsideVehicle()) {
			if(event.getPlayer().getVehicle() instanceof Horse){
				event.getPlayer().getVehicle().remove();
			}
		}
	}

	// Remove horse if rider is kicked
	@EventHandler
	public void riderKick(PlayerKickEvent event) {
		if (event.getPlayer().isInsideVehicle()) {
			if(event.getPlayer().getVehicle() instanceof Horse){
				event.getPlayer().getVehicle().remove();
			}
		}
	}

	// Cancel damage to horses
	@EventHandler
	void horseDamage(EntityDamageEvent event) {
		if (event.getEntity().getType().equals(EntityType.HORSE)) {
			event.setCancelled(true);
		}
	}

	// Block spawning of horses (bypassed temporarily by setting spawn=true [as
	// seen in CommandMethods.giveHorse()])
	@EventHandler(priority = EventPriority.HIGH)
	public void mobSpawn(CreatureSpawnEvent event) {
		if (event.getEntity().getType().equals(EntityType.HORSE)) {
			if (HorseUtil.spawn) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
			}
		}
	}
}
