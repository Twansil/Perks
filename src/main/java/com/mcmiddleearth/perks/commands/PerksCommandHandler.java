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
package com.mcmiddleearth.perks.commands;

import com.mcmiddleearth.perks.PerksPlugin;
import com.mcmiddleearth.perks.perks.Perk;
import com.mcmiddleearth.perks.permissions.PermissionData;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur, Ivanpl
 */
public abstract class PerksCommandHandler {
    
    private final String[] permissionNodes;
    
    @Getter
    private final int minArgs;
    
    private boolean playerOnly = true;
    
    @Getter
    private final Perk perk;
    
    public PerksCommandHandler(int minArgs, boolean playerOnly, Perk perk, String... permissionNodes) {
        this.minArgs = minArgs;
        this.playerOnly = playerOnly;
        this.permissionNodes = permissionNodes;
        this.perk = perk;
    }

    public abstract String getShortDescription(String subcommand);
    public abstract String getUsageDescription(String subcommand);
    
    public void handle(CommandSender cs, String cmd, String... args) {
        Player p = null;
        if(cs instanceof Player) {
            p = (Player) cs;
        }
        
        if(p == null && playerOnly) {
            sendPlayerOnlyErrorMessage(cs);
            return;
        }
        
        if(p != null && !hasPermissions(p)) {
            sendNoPermsErrorMessage(p);
            return;
        }
        
//Logger.getGlobal().info("Perk: "+perk+"  "+PerksPlugin.getInstance().isPerkEnabled(perk));
        if(perk!=null && !PerksPlugin.getInstance().isPerkEnabled(perk)) {
            sendNotEnabledErrorMessage(p);
            return;
        }
        
        if(args.length < minArgs) {
            sendMissingArgumentErrorMessage(cs);
            return;
        }
        
        execute(cs, cmd, args);
    }
    
    protected abstract void execute(CommandSender cs, String cmd, String... args);
    
    private void sendPlayerOnlyErrorMessage(CommandSender cs) {
        PerksPlugin.getMessageUtil().sendErrorMessage(cs, "You have to be logged in to run this command.");
    }
    
    private void sendNoPermsErrorMessage(CommandSender cs) {
        PerksPlugin.getMessageUtil().sendErrorMessage(cs, "You don't have permission to run this command.");
    }
    
    protected void sendMissingArgumentErrorMessage(CommandSender cs) {
        PerksPlugin.getMessageUtil().sendErrorMessage(cs, "You're missing arguments for this command.");
    }
    
    protected boolean hasPermissions(Player p) {
        if(permissionNodes != null) {
            for(String permission : permissionNodes) {
                if (!p.hasPermission(permission)) {
                    return false;
                }
            }
        }
        if(perk!=null) {
            return PermissionData.isAllowed(p, perk); 
        } else {
            return true;
        }
    }
    
    protected void sendNotEnabledErrorMessage(CommandSender cs){
        PerksPlugin.getMessageUtil().sendErrorMessage(cs, perk.getName()+" perk is currently disabled!");
    }
    
    protected void sentInvalidArgumentMessage(CommandSender cs) {
        PerksPlugin.getMessageUtil().sendErrorMessage(cs, "Invalid Argument");
    }
    
    public String disabledWarning() {
        if(PerksPlugin.getInstance().arePerksEnabled()) {
            return "";
        } else {
            return ChatColor.RED+" (But perks are disabled in general)";
        }
    }
}