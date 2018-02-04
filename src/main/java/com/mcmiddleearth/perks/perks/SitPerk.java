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
import com.mcmiddleearth.perks.listeners.SitListener;
import com.mcmiddleearth.perks.permissions.PermissionData;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class SitPerk extends Perk {
    
    private static final Map<Player, ArmorStand> armorStands = new HashMap<>();
    
    private static YamlConfiguration config = new YamlConfiguration();
    
    private static final File configFile = new File(PerksPlugin.getInstance().getDataFolder(),
                                                           "SitPerkConfiguration.yml");
    
    @Getter
    private static Material item;
    
    public SitPerk() {
        super("sit");
        try {
            config.load(configFile);
        } catch (IOException ex) {
            Logger.getLogger(SitPerk.class.getName()).log(Level.INFO, "SitPerkConfiguration.yml not found!");
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(SitPerk.class.getName()).log(Level.INFO, "SitPerkConfiguration.yml not valid!");
        }
        item = Material.valueOf(PerksPlugin.getPerkString(this.getName(),"item","GHAST_TEAR"));
        setListener(new SitListener());
    }
    
    public static void sitDown(Player player, Block clicked) {
        double xshift = 0.5;
        double zshift = 0.5;
        double yshift = -0.75;
        xshift = xshift + (hasSpecialXAdjust(clicked)?getSpecialXAdjust(clicked):getXBlockAdjust(clicked));
        yshift = yshift + (hasSpecialYAdjust(clicked)?getSpecialYAdjust(clicked):getYBlockAdjust(clicked));
        zshift = zshift + (hasSpecialZAdjust(clicked)?getSpecialZAdjust(clicked):getZBlockAdjust(clicked));
        Location pLoc = player.getLocation();
        Float yaw = (hasSpecialYaw(clicked)?getSpecialYaw(clicked):getTurnAround(pLoc,clicked));
        Location turnedAround = new Location(pLoc.getWorld(), pLoc.getX(), pLoc.getY(),pLoc.getZ(),
                                             yaw, pLoc.getPitch());
        Location loc = new Location(clicked.getWorld(),
                                    clicked.getX()+xshift,
                                    clicked.getY()+yshift,
                                    clicked.getZ()+zshift,
                                    turnedAround.getYaw(), 0);
        ArmorStand armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        player.teleport(turnedAround);
        armor.setGravity(false);
        armor.setVisible(false);
        armor.setPassenger(player);
        armorStands.put(player,armor);
        armor.setCustomName("Seat_Marker_remove_if found_without_a_player_mounted!");
    }
    
    public static void sitUp(Player player) {
        ArmorStand marker = armorStands.get(player);
        if(marker!=null) {
            if(player.isInsideVehicle() 
                    && player.getVehicle().equals(marker)) {
                player.eject();
            }
            marker.remove();
        }
        armorStands.remove(player);
    }
    
    @Override
    public void disable() {
        for(Player player: armorStands.keySet()) {
            sitUp(player);
        }
    }

    @Override
    public void check() {
        for(Player player:armorStands.keySet()) {
            if(!PermissionData.isAllowed(player, this)) {
                sitUp(player);
            }
        }
    }
    
    @Override
    public void writeDefaultConfig(ConfigurationSection config) {
        config.set("item",item.name());
    }
    
    private static Float getTurnAround(Location pLoc, Block clicked) {
        if(isStairBlock(clicked)) {
            return(getStairYaw(clicked));
            //return  new Location(pLoc.getWorld(), pLoc.getX(), pLoc.getY(),pLoc.getZ(),
              //                          getStairYaw(clicked), pLoc.getPitch());
        } else {
            return pLoc.getYaw()+180;
            //return new Location(pLoc.getWorld(), pLoc.getX(), pLoc.getY(),pLoc.getZ(),
              //                          pLoc.getYaw()+180, pLoc.getPitch());
        }
    }
    
    private static double getXBlockAdjust(Block clicked) {
        if(isStairBlock(clicked)) {
            return stairShiftX(clicked);
        }
        return 0;
    }
    
    private static double getZBlockAdjust(Block clicked) {
        if(isStairBlock(clicked)) {
            return stairShiftZ(clicked);
        }
        return 0;
    }
    
    private static double getYBlockAdjust(Block clicked) {
        if(clicked.getType().equals(Material.SNOW)) {
            return -0.875+(1/8.0)*clicked.getData();
        }
        if(isHalfBlock(clicked) || isStairBlock(clicked)) {
            return -0.44;
        }
        if(isQuarterBlock(clicked)) {
            return -0.68;
        }
        if(isCarpet(clicked)) {
            return -0.9;
        }
        if(isUnsolid(clicked)) {
            return -1;
        }
        if(isThreeQuarterBlock(clicked)){
            return -0.22;
        }
        return 0;
    }
    
    private static boolean isUnsolid(Block clicked) {
        switch(clicked.getType()) {
            case LONG_GRASS:
            case SAPLING:
            case YELLOW_FLOWER:
            case RED_ROSE:
            case RED_MUSHROOM:
            case BROWN_MUSHROOM:
            case CROPS:
            case POTATO:
            case CARROT:
            case REDSTONE_WIRE:
            case TRIPWIRE_HOOK:
            case NETHER_WARTS:
            case MELON_STEM:
            case PUMPKIN_STEM:
            case STRING:
            case FLOWER_POT:
            case WALL_SIGN:
            case WEB:
            case DEAD_BUSH:
            case TORCH:
            case REDSTONE_TORCH_ON:
            case REDSTONE_TORCH_OFF:
            case LEVER:
            case WOOD_BUTTON:
            case STONE_BUTTON:
            case VINE:
            case DOUBLE_PLANT:
            case LADDER:
            case RAILS:
            case POWERED_RAIL:
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
            case WALL_BANNER:
            case CAKE_BLOCK:
            case BEETROOT_BLOCK:
                return true;
        }
        return false;
    }
    
    private static boolean isThreeQuarterBlock(Block clicked) {
        switch(clicked.getType()) {
            case ENCHANTMENT_TABLE:
            case ENDER_PORTAL_FRAME:
                return true;
        }
        return false;
    }
    
    private static boolean isCarpet(Block clicked) {
        switch(clicked.getType()) {
            case CARPET:
            case WOOD_PLATE:
            case STONE_PLATE:
            case IRON_PLATE:
            case GOLD_PLATE:
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_COMPARATOR_OFF:
                return true;
        }
        return false;
    }
    
    private static boolean isQuarterBlock(Block clicked) {
        switch(clicked.getType()) {
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
                return true;
        }
        return false;
    }
        
    private static boolean isHalfBlock(Block clicked) {
        switch(clicked.getType()) {
            case SKULL:
            case BED_BLOCK:
                return true;
        }
        if(clicked.getData()>7) {
                return false;
        }
        if(clicked.getType().equals(Material.CAKE_BLOCK)
                && clicked.getData()<5) {
            return true;
        }
        switch(clicked.getType()) {
            case STEP:
            case BED_BLOCK:
            case WOOD_STEP:
            case STONE_SLAB2:
            case PURPUR_SLAB:
                return true;
        }
        return false;
    }
    private static boolean isStairBlock(Block clicked) {
        if(clicked.getData()>3) {
            return false;
        }
        switch(clicked.getType()) {
            case WOOD_STAIRS:
            case SANDSTONE_STAIRS:
            case BRICK_STAIRS:
            case COBBLESTONE_STAIRS:
            case SPRUCE_WOOD_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case ACACIA_STAIRS:
            case DARK_OAK_STAIRS:
            case QUARTZ_STAIRS:
            case RED_SANDSTONE_STAIRS:
            case PURPUR_STAIRS:
                return true;
        }
        return false;
    }
    
    private static float getStairYaw(Block clicked) {
        switch(clicked.getData()) {
            case 0:
                return 90;
            case 1:
                return 270;
            case 2:
                return 180;
            case 3:
                return 0;
        }
        return 0;
    }
    
    private static double stairShiftX(Block clicked) {
        switch(clicked.getData()) {
            case 0:
                return -0.1;
            case 1:
                return 0.1;
        }
        return 0;
    }
    private static double stairShiftZ(Block clicked) {
        switch(clicked.getData()) {
            case 2:
                return -0.1;
            case 3:
                return 0.1;
        }
        return 0;
    }
    
    private static ConfigurationSection getConfigSection(Block clicked) {
        ConfigurationSection section = config.getConfigurationSection(clicked.getType().name());
        if(section == null) {
            for(String key: config.getKeys(false)) {
            }
            section = config.getConfigurationSection(clicked.getType().name()+"_"+clicked.getData());
        }
        return section;
    }
    
    private static boolean hasSpecialValue(Block clicked, String key) {
        ConfigurationSection section = getConfigSection(clicked);
        if(section != null) {
            return section.contains(key);
        }
        return false;
    }
    
    private static boolean hasSpecialYaw(Block clicked) {
        return hasSpecialValue(clicked, "Yaw");
    }
    
    private static Float getSpecialYaw(Block clicked) {
        ConfigurationSection section = getConfigSection(clicked);
        return Float.parseFloat(section.getString("Yaw"));
    }
    
    private static boolean hasSpecialXAdjust(Block clicked) {
        return hasSpecialValue(clicked, "X");
    }
    
    private static Double getSpecialXAdjust(Block clicked) {
        ConfigurationSection section = getConfigSection(clicked);
        return section.getDouble("X");
    }
    
    private static boolean hasSpecialYAdjust(Block clicked) {
        return hasSpecialValue(clicked, "Y");
    }
    
    private static Double getSpecialYAdjust(Block clicked) {
        ConfigurationSection section = getConfigSection(clicked);
        return section.getDouble("Y");
    }
    
    private static boolean hasSpecialZAdjust(Block clicked) {
        return hasSpecialValue(clicked, "Z");
    }
    
    private static Double getSpecialZAdjust(Block clicked) {
        ConfigurationSection section = getConfigSection(clicked);
        return section.getDouble("Z");
    }
    
}
    