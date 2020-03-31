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

import com.mcmiddleearth.perks.PerksPlugin;
import com.mcmiddleearth.perks.perks.ItemPerk;
import com.mcmiddleearth.perks.permissions.PermissionData;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * 
 * @author Eriol_Eandur
 */

public class ItemListener implements Listener {

    private final ItemPerk perk;
    
    public ItemListener(ItemPerk perk) {
        this.perk = perk;
    }

    private boolean checkForItem(Player p) {
        if(!perk.isEnabled()) {
            PerksPlugin.getMessageUtil().sendErrorMessage(p,perk.getName()+" perk is not enabled.");
            perk.removeItems(p);
            return false;
        }
        if((!PermissionData.isAllowed(p, perk))) {
            PerksPlugin.getMessageUtil().sendErrorMessage(p,perk.getName()+" is not given to you.");
            perk.removeItems(p);
            return false;
        }
        //perk.check(p);
        return true;
    }

    // Checks if user has permission to use an item. Otherwise removes all elytras from his inventory.
    @EventHandler
    public void blockItemClick(InventoryClickEvent event) {
//Logger.getGlobal().info("Item block click test: "+event.getCurrentItem());
//Logger.getGlobal().info("Clicked Inventory: "+event.getClickedInventory().getName()+"   "+event.getClickedInventory().getHolder().toString());
        if(!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        if(!(event.getCurrentItem()!=null
                && event.getCurrentItem().getType().equals(perk.getItemMaterial()))) {
            return;
        }
//Logger.getGlobal().info("Item block click: "+perk.getItemMaterial());
        if(!checkForItem(p)) {
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void blockItemDrag(InventoryDragEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        if(!(event.getCursor()!=null
                && event.getCursor().getType().equals(perk.getItemMaterial()))) {
            return;
        }
//Logger.getGlobal().info("Item block drag: "+perk.getItemMaterial());
        if(!checkForItem(p)) {
            event.setCancelled(true);
            event.setCursor(new ItemStack(Material.AIR));
        }
    }
    
    @EventHandler
    public void blockItemOpen(InventoryOpenEvent event) {
        if(!(event.getPlayer() instanceof Player)) {
            return;
        }
//Logger.getGlobal().info("Item block open: "+perk.getItemMaterial());
        perk.check((Player) event.getPlayer());
    }
    
    @EventHandler
    public void blockItemClose(InventoryCloseEvent event) {
        if(!(event.getPlayer() instanceof Player)) {
            return;
        }
//Logger.getGlobal().info("Item block open: "+perk.getItemMaterial());
        perk.check((Player) event.getPlayer());
    }
    
    @EventHandler
    public void blockItemSwap(PlayerSwapHandItemsEvent event) {
        PlayerInventory inv = event.getPlayer().getInventory();
        if(!((inv.getItemInMainHand()!=null && inv.getItemInMainHand().getType().equals(perk.getItemMaterial()))
                ||(inv.getItemInOffHand()!=null && inv.getItemInOffHand().getType().equals(perk.getItemMaterial())))) {
            return;
        }
//Logger.getGlobal().info("Item block swap hand: "+perk.getItemMaterial());
        checkForItem(event.getPlayer());
    }
    
    @EventHandler
    public void blockItemHeld(PlayerItemHeldEvent event) {
        PlayerInventory inv = event.getPlayer().getInventory();
        ItemStack oldItem = inv.getItem(event.getPreviousSlot());
        ItemStack newItem = inv.getItem(event.getNewSlot());
        if(!((oldItem!=null && oldItem.getType().equals(perk.getItemMaterial()))
                ||(newItem!=null && newItem.getType().equals(perk.getItemMaterial())))) {
            return;
        }
//Logger.getGlobal().info("Item block change held: "+perk.getItemMaterial());
        checkForItem(event.getPlayer());
    }
   
    @EventHandler
    public void blockItemUse(PlayerInteractEvent event) {
        Player p = (Player) event.getPlayer();
        PlayerInventory inv = p.getInventory();
        if(!((inv.getItemInMainHand()!=null && inv.getItemInMainHand().getType().equals(perk.getItemMaterial()))
                ||(inv.getItemInOffHand()!=null && inv.getItemInOffHand().getType().equals(perk.getItemMaterial())))) {
            return;
        }
//Logger.getGlobal().info("Item block use: "+perk.getItemMaterial());
        if(!checkForItem(p)) {
            event.setCancelled(true);
        }
    }

    
    
}
