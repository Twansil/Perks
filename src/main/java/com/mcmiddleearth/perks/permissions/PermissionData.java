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
package com.mcmiddleearth.perks.permissions;

import com.mcmiddleearth.perks.PerkManager;
import com.mcmiddleearth.perks.PerksPlugin;
import com.mcmiddleearth.perks.perks.Perk;
import com.mcmiddleearth.pluginutil.NumericUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Eriol_Eandur
 */
public class PermissionData {

    private final static File creditDataFile = new File(PerksPlugin.getInstance().getDataFolder(),"creditData.yml");
    private final static File creditDefinitionFile= new File(PerksPlugin.getInstance().getDataFolder(),"creditDefinition.yml");
    
    private static final YamlConfiguration creditDefinitionConfig = new YamlConfiguration();
    
    private static final Map<UUID, List<String>> creditData = new HashMap<>();
    
    private static final Set<Perk> freePerks = new HashSet<>();
    
    public static void load() {
        try {
            creditDefinitionConfig.load(creditDefinitionFile);
            try {
                YamlConfiguration creditDataConfig = new YamlConfiguration();
                creditDataConfig.load(creditDataFile);
                updateCredits(creditDataConfig);
            } catch (IOException | InvalidConfigurationException ex) {
                Logger.getLogger(PermissionData.class.getName()).log(Level.SEVERE, "Error reading credit data.", ex);
            }
        } catch (IOException | InvalidConfigurationException ex) {
            Logger.getLogger(PermissionData.class.getName()).log(Level.SEVERE, "Error reading credit definitions.", ex);
        }
    }
    
    private static void saveCreditData() {
        try {
            YamlConfiguration creditDataConfig = new YamlConfiguration();
            for(UUID playerID: creditData.keySet()) {
                creditDataConfig.set(playerID.toString(), creditData.get(playerID));
            }
            creditDataConfig.save(creditDataFile);
        } catch (IOException ex) {
            Logger.getLogger(PermissionData.class.getName()).log(Level.SEVERE, "Error while saving credit Data.");
        }
    }
    
    public static synchronized void updateCredits(Configuration newCredits) {
        creditData.clear();
        for(String playerID:newCredits.getKeys(false)) {
            List<String> credits = newCredits.getStringList(playerID);
            creditData.put(UUID.fromString(playerID), credits);
/*for(String credit: credits) {
Logger.getGlobal().info("Giving permission to "+ playerID+" for credit "+credit);
}*/
        }
        saveCreditData();
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player: Bukkit.getOnlinePlayers()) {
                    updatePerkPermissions(player);
                }
                for(Perk perk: PerkManager.getPerks()) {
                    perk.check();
                }
            }
        }.runTask(PerksPlugin.getInstance());
    }
    
    public static void updatePerkPermissions(Player player) {
        PermissionAttachment attachment = getPermissionAttachment(player);
        player.removeAttachment(attachment);
        List<String> credits = creditData.get(player.getUniqueId());
        if(credits==null) {
            return;
        }
        for(String creditKey: credits) {
//Logger.getGlobal().info("Set perk perms for: "+player.getName()+" - "+creditKey);
            setPermissions(player.getUniqueId(), creditKey, true);
        }
        player.recalculatePermissions();
    }
    
    private static void setPermissions(UUID playerId, String creditKey, boolean give) {
        Player player = Bukkit.getPlayer(playerId);
        if(player==null) {
            return;
        }
//Logger.getGlobal().info("1");
        PermissionAttachment attachment = getPermissionAttachment(player);
        String[] data = creditKey.split("_");
//Logger.getGlobal().info(data[0]+ " "+ data[1]);
//Logger.getGlobal().info("Definition: "+creditDefinitionConfig.getName());
        ConfigurationSection section = creditDefinitionConfig.getConfigurationSection(data[0]);
//Logger.getGlobal().info(""+section);
        if(section==null || data.length<2 || !NumericUtil.isInt(data[1])) {
            return;
        }
//Logger.getGlobal().info(data[0]+ " "+ data[1]);
        for(String value: section.getKeys(false)) {
//Logger.getGlobal().info("section key: "+value);
            if(NumericUtil.isInt(value) 
                    && NumericUtil.getInt(value)<=NumericUtil.getInt(data[1])) {
                List<String> perks = section.getStringList(value);
//Logger.getGlobal().info(perks.toString());
                if(perks!=null) {
                    for(String perkName:perks) {
                        Perk perk = PerkManager.forName(perkName);
//Logger.getGlobal().info(perk.getName());
                        if(perk!=null) {
//Logger.getGlobal().info("         Set perm: "+perk.getPermissionNode());
                            attachment.setPermission(perk.getPermissionNode(), give);
                        }
                    }
                }
            }
        }
    }
    
    private static PermissionAttachment getPermissionAttachment(Player player) {
        Set<PermissionAttachmentInfo> infos = player.getEffectivePermissions();
        for(PermissionAttachmentInfo info:infos) {
            if(info.getAttachment()!=null
                    && info.getAttachment().getPlugin()==PerksPlugin.getInstance()) {
                return info.getAttachment();
            }
        }
        return player.addAttachment(PerksPlugin.getInstance());
    }
    
    public static boolean isAllowed(Player player, Perk perk) {
        return player.hasPermission(Permissions.USER.getPermissionNode()) 
                && (player.hasPermission(perk.getPermissionNode()) || freePerks.contains(perk));
    }
    
    /**
     * Makes a perk available for everyone with Permission.USER.
     * @param perk
     * @param duration 
     */
    public static void enableFreePerk(final Perk perk, int duration) {
        freePerks.add(perk);
        new BukkitRunnable() {
            @Override
            public void run() {
                disableFreePerk(perk);
            }
        }.runTaskLater(PerksPlugin.getInstance(), duration*60*20);
    }

    public static void disableFreePerk(Perk perk) {
        freePerks.remove(perk);
    }
}
