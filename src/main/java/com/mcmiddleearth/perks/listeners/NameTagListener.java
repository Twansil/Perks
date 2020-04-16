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

import com.mcmiddleearth.perks.perks.NameTagPerk;
import com.mcmiddleearth.perks.permissions.PermissionData;

import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Eriol_Eandur
 */

public class NameTagListener implements Listener {

    NameTagPerk perk;

    public NameTagListener(NameTagPerk perk) {
        this.perk = perk;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoin(PlayerJoinEvent event) {
        if (perk.isEnabled()
                && PermissionData.isAllowed(event.getPlayer(), perk)) {
            perk.setNameTag(event.getPlayer(), true);
        }
    }

}
