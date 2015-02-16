/*
 * Copyright (c) 2015 dags_ & meggawatts
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
