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

import com.mcmiddleearth.perks.PerkManager;
import com.mcmiddleearth.perks.PerksPlugin;
import com.mcmiddleearth.perks.perks.JockeyPerk;
import com.mcmiddleearth.perks.perks.Perk;
import com.mcmiddleearth.perks.permissions.PermissionData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class JockeyListener implements Listener {


    @EventHandler
    public void playerInteractPlayer(PlayerInteractEntityEvent event) {
        if (!(event.getHand().equals(EquipmentSlot.HAND)
                && event.getRightClicked() instanceof Player)) {
            return;
        }
        //Eject a jockey by right-clicking at him
        if (event.getRightClicked().isInsideVehicle()) {
            Player jockey = (Player) event.getRightClicked();
            if (jockey.getVehicle() instanceof Player) {
                Player ride = (Player) jockey.getVehicle();
                if (ride.equals(event.getPlayer())) {
                    event.setCancelled(true);
                    unjockey(jockey);
                }
            }
            return;
        }
        //Unjockey from another player by right-clicking at him
        Player jockey = event.getPlayer();
        if (jockey.isInsideVehicle() && (jockey.getVehicle() instanceof Player)) {
            event.setCancelled(true);
            unjockey(jockey);
            return;
        }

        //Jockey another player by right-clicking at him
        if (!jockey.isInsideVehicle()
                && jockey.getInventory().getItemInMainHand().getType()
                .equals(JockeyPerk.getItemMaterial())) {
            event.setCancelled(true);
            Perk perk = PerkManager.forName("jockey");
            if (!perk.isEnabled()) {
                PerksPlugin.getMessageUtil().sendErrorMessage(jockey, "Jockey perk is not enabled.");
                return;
            }
            if ((!PermissionData.isAllowed(jockey, perk))) {
                PerksPlugin.getMessageUtil().sendErrorMessage(jockey, "Jockey perk is not given to you.");
                return;
            }
            Player q = (Player) event.getRightClicked();//ride;
            if (!q.hasPermission(JockeyPerk.getAntiJockeyPermission())) {
                q.addPassenger(jockey);
                PerksPlugin.getMessageUtil().sendInfoMessage(jockey, "Now riding "
                        + PerksPlugin.getMessageUtil().STRESSED + q.getName()
                        + PerksPlugin.getMessageUtil().INFO + "!");
            } else {
                PerksPlugin.getMessageUtil().sendErrorMessage(jockey, "You cannot ride that player!");
            }
        }
    }

    private void unjockey(Player jockey) {
        jockey.getVehicle().eject();
        PerksPlugin.getMessageUtil().sendInfoMessage(jockey, "You have been ejected!");
    }

    @EventHandler
    public void riderQuit(PlayerQuitEvent event) {
        if (event.getPlayer().isInsideVehicle()) {
            if (event.getPlayer().getVehicle() instanceof Player) {
                event.getPlayer().getVehicle().eject();
            }
        }
    }

    @EventHandler
    public void riderKick(PlayerKickEvent event) {
        if (event.getPlayer().isInsideVehicle()) {
            if (event.getPlayer().getVehicle() instanceof Player) {
                event.getPlayer().getVehicle().eject();
            }
        }
    }

}
