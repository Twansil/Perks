/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.perks.listeners;

import com.mcmiddleearth.perks.PerkManager;
import com.mcmiddleearth.perks.perks.BoatPerk;
import com.mcmiddleearth.perks.perks.CompassPerk;
import java.util.logging.Logger;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

/**
 *
 * @author Fraspace5
 */
public class CompassListener implements Listener {
    
      
    // Checks if user getting on the horse has permission, and that the horse is
    // his. Otherwise removes horse.
    @EventHandler
    public void PlayerLeave(PlayerQuitEvent event) {
        ((CompassPerk)PerkManager.forName("compass")).unsetCompassDirection(event.getPlayer());
    }
}
