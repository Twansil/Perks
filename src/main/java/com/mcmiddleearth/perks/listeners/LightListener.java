/*
 * Copyright (C) 2017 MCME
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mcmiddleearth.perks.listeners;

import com.mcmiddleearth.perks.perks.WizardLightPerk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * 
 * @author Eriol_Eandur
 */

public class LightListener implements Listener {

    @EventHandler
    public void playerDie(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            WizardLightPerk.unsummonLight((Player)event.getEntity());
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        WizardLightPerk.unsummonLight(event.getPlayer());
    }

    @EventHandler
    public void playerKick(PlayerKickEvent event) {
        WizardLightPerk.unsummonLight(event.getPlayer());
    }

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent event) {
        WizardLightPerk.unsummonLight(event.getPlayer());
    }
    
    @EventHandler(ignoreCancelled=false)
    public void waveWand(PlayerInteractEvent event) {
        if(event.hasItem() && event.getItem().getType().equals(WizardLightPerk.getItem())) {
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                WizardLightPerk.increaseDistance(event.getPlayer());
            } else if(event.getAction().equals(Action.LEFT_CLICK_AIR)) {
                WizardLightPerk.decreaseDistance(event.getPlayer());
            }    
        }
    }

}
