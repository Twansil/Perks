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

import com.mcmiddleearth.perks.commands.HorseHandler;
import com.mcmiddleearth.perks.listeners.HorseListener;
import com.mcmiddleearth.perks.permissions.PermissionData;
import com.mcmiddleearth.perks.permissions.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

/**
 *
 * @author Eriol_Eandur
 */
public class HorsePerk extends Perk {
    
    private static Perk instance;
    
    public static final String horse_perk_custom_Name = "'s Sweet Ride!";
    private static boolean allowSpawn;
    
    public HorsePerk() {
        super("horse");
        setListener(new HorseListener());
        setCommandHandler(new HorseHandler(this, Permissions.USER.getPermissionNode()),"horse");
    }
  
    public void clearHorses() {
        for (World w : Bukkit.getServer().getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (isHorsePerk(e)) {
                    e.remove();
                }
            }
        }
    }
    
    public void checkHorses() {
        for (World w : Bukkit.getServer().getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (isHorsePerk(e) && (((Vehicle)e).getPassenger()==null
                                       || !PermissionData.isAllowed((Player)((Vehicle)e).getPassenger(),
                                                                   this))) {
                    e.remove();
                }
            }
        }
    }
        
    public static boolean isHorsePerk(Entity entity) {
        return entity.getCustomName()!=null
                && entity.getCustomName().contains(horse_perk_custom_Name)
                && entity.getType().equals(EntityType.HORSE);
    }

    public static void allowSpawn(boolean allow) {
        allowSpawn = allow;
    }
    
    @Override
    public void disable() {
        clearHorses();
    }
    
    @Override
    public void check() {
        checkHorses();
    }

    public static Perk getInstance() {
        return instance;
    }

    public static boolean isAllowSpawn() {
        return allowSpawn;
    }
}
