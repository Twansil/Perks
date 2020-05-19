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
package com.mcmiddleearth.perks.perks;

import com.mcmiddleearth.perks.PerksPlugin;
import com.mcmiddleearth.perks.commands.CompassHandler;
import com.mcmiddleearth.perks.listeners.CompassListener;
import com.mcmiddleearth.perks.permissions.Permissions;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

/**
 *
 * @author Fraspace5
 */
public class CompassPerk extends Perk {
    
    Map<Player,BukkitTask> compassTasks = new HashMap<>();
    
    public CompassPerk() {
        super("compass");
        setListener(new CompassListener());
        setCommandHandler(new CompassHandler(this, Permissions.USER.getPermissionNode()),"compass");
    }
    
    public void setCompassDirection(Player player, float targetYaw) {
        unsetCompassDirection(player);
        BukkitTask task = new CompassRunnable(player,targetYaw).runTaskTimer(PerksPlugin.getInstance(), 0, 20);
        compassTasks.put(player, task);
    }
    
    public void unsetCompassDirection(Player player) {
        BukkitTask task = compassTasks.get(player);
        if(task!=null && !task.isCancelled()) {
            task.cancel();
        }
        compassTasks.remove(player);
    }
    
    public class CompassRunnable extends BukkitRunnable {

        private Vector direction;
        private Player player;
        private final int distance = 1400;
        
        public CompassRunnable(Player player, double targetYaw) {
            targetYaw = (targetYaw * Math.PI) / 180;
            direction = new Vector(Math.sin(targetYaw)*distance,0,-Math.cos(targetYaw)*distance);
            this.player = player;
        }
        
        @Override
        public void run() {
            if(player==null || !player.isOnline()) {
                cancel();
            } else {
                player.setCompassTarget(player.getLocation().add(direction));
            }
        }
    
    }
 
}
