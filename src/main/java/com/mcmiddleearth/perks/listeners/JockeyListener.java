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

import static com.mcmiddleearth.perks.MCMEPerks.prim;
import static com.mcmiddleearth.perks.MCMEPerks.scd;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JockeyListener implements Listener{
	
	public static void jockey(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if(p.isInsideVehicle()){
			p.getVehicle().eject();
			p.sendMessage(scd + "You have been ejected!");
		} else { 
			Entity ride = null;
			for (Entity e : event.getPlayer()
					.getNearbyEntities(3, 3, 3)) {
				if (e instanceof Player) {
					if (!e.isInsideVehicle()) {
						ride = e;
					}
				}
			}
			if (ride != null) {
				Player q = (Player) ride;
				if(!q.hasPermission("perks.antijockey")){
					ride.setPassenger(p);
					p.sendMessage(prim + "Now riding "+ scd + q.getName() + prim + "!");
				} else {
					p.sendMessage(scd + "You cannot ride that player!");
				}
			} else {
				p.sendMessage(scd + "There are no nearby players to ride!");
			}
		}
	}
	
	@EventHandler
	private void eject(PlayerInteractEntityEvent event){
		if(event.getRightClicked() instanceof Player
				&& event.getRightClicked().isInsideVehicle()){
			Player jockey = (Player) event.getRightClicked();
			if(jockey.getVehicle() instanceof Player){
				Player ride = (Player) jockey.getVehicle();
				if(ride.equals(event.getPlayer())){
					jockey.getVehicle().eject();
					jockey.sendMessage(scd + "You have been ejected!");
				}
			}
		}
	}
	
	@EventHandler
	public void riderQuit(PlayerQuitEvent event) {
		if (event.getPlayer().isInsideVehicle()) {
			if(event.getPlayer().getVehicle() instanceof Player){
				event.getPlayer().getVehicle().eject();
			}
		}
	}

	@EventHandler
	public void riderKick(PlayerKickEvent event) {
		if (event.getPlayer().isInsideVehicle()) {
			if(event.getPlayer().getVehicle() instanceof Player){
				event.getPlayer().getVehicle().eject();
			}
		}
	}

}
