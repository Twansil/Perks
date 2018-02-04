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

import com.mcmiddleearth.perks.PerkManager;
import com.mcmiddleearth.perks.PerksPlugin;
import com.mcmiddleearth.perks.perks.Perk;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Eriol_Eandur
 */
public class EnableHandler extends PerksCommandHandler {

    public EnableHandler(String... permissionNodes) {
        super(0,false,null,permissionNodes);
    }
    
    @Override
    public String getShortDescription(String cmd) {
        if(cmd.equalsIgnoreCase("enable"))
            return ": "+PerksPlugin.getMessageUtil().INFO+"Enables perks.";
        else if(cmd.equalsIgnoreCase("disable"))
            return ": "+PerksPlugin.getMessageUtil().INFO+"Disables Perks.";
        else
            return ": "+PerksPlugin.getMessageUtil().INFO+"Displays status of all perks.";
    }
    
    @Override
    public String getUsageDescription(String cmd) {
        if(cmd.equalsIgnoreCase("enable"))
            return "[perk]: Enables the perk named [perk] or all perks when no argument is specified.";
        else if(cmd.equalsIgnoreCase("disable"))
            return "[perk]: Disables the perk named [perk] or all perks when no argument is specified.";
        else
            return ": Displays for each perk if it is enabled or disabled. Also shows if perks are "
                    +"enabled in general.";
    }
   
    @Override
    protected void execute(CommandSender cs, String cmd, String... args) {
        PerksPlugin plugin = PerksPlugin.getInstance();
        if(cmd.equals("info")) {
            PerksPlugin.getMessageUtil().sendInfoMessage(cs, "Usage of Perks is "
                                                             +enabledMessage(plugin.arePerksEnabled())+".");
            if(plugin.arePerksEnabled()) {
                for(Perk search: PerkManager.getPerks()) {
                    PerksPlugin.getMessageUtil()
                          .sendIndentedInfoMessage(cs, 
                                search.getName()+" is "
                               +enabledMessage(plugin.isPerkEnabled(search)));
                }
            }
            return;
        }
        if(args.length==0) {
            if(cmd.equals("enable")) {
                plugin.enableAllPerks(true);
                PerksPlugin.getMessageUtil().sendInfoMessage(cs, "Usage of perks "+ChatColor.GREEN+"enabled.");
                for(Perk perk: PerkManager.getPerks()) {
                    if(plugin.isPerkEnabled(perk)) {
                        perk.enable();
                    }
                }
                return;
            } else {
                plugin.enableAllPerks(false);
                for(Perk perk: PerkManager.getPerks()) {
                    perk.disable();
                }
                PerksPlugin.getMessageUtil().sendInfoMessage(cs, "Usage of perks "+ChatColor.RED+"disabled.");
                return;
            }
        }
        boolean enable = true;
        if(cmd.equals("disable")) {
            enable = false;
        }
        for(Perk search: PerkManager.getPerks()) {
            if(search.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                plugin.enablePerk(search, enable);
                if(enable) {
                    search.enable();
                    PerksPlugin.getMessageUtil().sendInfoMessage(cs, 
                            search.getName()+" perk "+ChatColor.GREEN+"enabled"
                            +PerksPlugin.getMessageUtil().INFO+"."
                            +this.disabledWarning());
                } else {
                    search.disable();
                    PerksPlugin.getMessageUtil().sendInfoMessage(cs, 
                            search.getName()+" perk "+ChatColor.RED+"disabled"
                            +PerksPlugin.getMessageUtil().INFO+".");
                }
                return;
            }
        }
        sentInvalidArgumentMessage(cs);
    }
    
    private String enabledMessage(boolean enabled) {
        return (enabled?ChatColor.GREEN+"enabled":ChatColor.RED+"disabled");
    }
    
}
