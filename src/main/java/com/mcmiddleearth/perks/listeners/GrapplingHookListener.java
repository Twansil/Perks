/*
 * Copyright (c) 2015 Dallen
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

import static com.mcmiddleearth.perks.MCMEPerks.scd;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Dallen
 */
public class GrapplingHookListener implements Listener {

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e){
        if(e.getEntity().getShooter() instanceof Player){
            Player shooter = (Player) e.getEntity().getShooter();
            if(shooter.getItemInHand().getType().equals(Material.FISHING_ROD) 
                    && shooter.getItemInHand().hasItemMeta()){
                ItemMeta im = shooter.getItemInHand().getItemMeta();
                if(im.hasLore() && im.hasDisplayName() && im.hasEnchants()){
                    if(im.getDisplayName().equalsIgnoreCase(ChatColor.RED + "Grappling Hook Perk")
                            && im.getLore().contains(ChatColor.DARK_PURPLE + shooter.getName())
                            && im.hasEnchant(Enchantment.DURABILITY)){
                        if(!shooter.hasPermission("perks.item.hookshot")){
                            shooter.sendMessage(scd + "Sorry, you cannot use that perk item!");
                            shooter.getInventory().remove(shooter.getItemInHand());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent e){
        Player p = e.getPlayer();
        if(p.getItemInHand().getType().equals(Material.FISHING_ROD) 
                && p.getItemInHand().hasItemMeta()){
            ItemMeta im = p.getItemInHand().getItemMeta();
            if(im.hasLore() && im.hasDisplayName() && im.hasEnchants()){
                if(im.getDisplayName().equalsIgnoreCase(ChatColor.RED + "Grappling Hook Perk")
                        && im.getLore().contains(ChatColor.DARK_PURPLE + p.getName())
                        && im.hasEnchant(Enchantment.DURABILITY)){
                    if(p.hasPermission("perks.item.hookshot")){
                        if(e.getState().equals(PlayerFishEvent.State.IN_GROUND)
                                || e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)){
                            Location dest = e.getHook().getLocation();
                            dest.setPitch(p.getLocation().getPitch());
                            dest.setYaw(p.getLocation().getYaw());
//                            System.out.print(dest.toString());
//                            p.setVelocity(VelocityUtil.calculateVelocity(p.getLocation().toVector(), dest.toVector(), 6));
                            p.teleport(dest);
                        }
                    }else{
                        p.sendMessage(scd + "Sorry, you cannot use that perk item!");
                        p.getInventory().remove(p.getItemInHand());
                    }
                }
            }
        }
    }
}
