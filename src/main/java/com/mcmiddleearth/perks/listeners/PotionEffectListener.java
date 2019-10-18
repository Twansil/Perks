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

import com.mcmiddleearth.perks.PerkManager;
import com.mcmiddleearth.perks.PerksPlugin;
import com.mcmiddleearth.perks.perks.Perk;
import com.mcmiddleearth.perks.perks.PotionEffectPerk;
import com.mcmiddleearth.perks.permissions.PermissionData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * 
 * @author Eriol_Eandur
 */

public class PotionEffectListener implements Listener {

    private final String name;
    
    public PotionEffectListener(String name) {
        this.name = name;
    }
    
    @EventHandler
    public void rightClickPerkItem(PlayerInteractEvent event) {
        if(!((event.getAction().equals(Action.RIGHT_CLICK_AIR)
                || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
             && event.hasItem()
             && event.getItem().hasItemMeta()
             && event.getItem().getItemMeta().hasLore())) {
            return;
        }
        String itemName = event.getItem().getItemMeta().getLore().get(0);
        if(!itemName.equals(name)) {
            return;
        }
        event.setCancelled(true);
        Perk perk = PerkManager.forName(name);
        if(perk==null 
                || !(perk instanceof PotionEffectPerk)) {
            return;
        }
        PotionEffectPerk potionPerk = (PotionEffectPerk) perk;
        Player p = event.getPlayer();
//Logger.getGlobal().info("PotionEfectListener Perk found");
        if(potionPerk.isActive(p)) {
            potionPerk.removeEffect(p);
            return;
        }
//Logger.getGlobal().info("PotionEfectListener add effect");
        if(!PerkManager.isEnabled(perk.getName())) {
            PerksPlugin.getMessageUtil().sendErrorMessage(p, "The "+name+" perk is not enabled.");
            return;
        }
        if(!PermissionData.isAllowed(event.getPlayer(), perk)) {
            PerksPlugin.getMessageUtil().sendErrorMessage(p, "The "+name+" perk was not given to you.");
            return;
        }
        ((PotionEffectPerk)perk).giveEffect(event.getPlayer());
    }

}
