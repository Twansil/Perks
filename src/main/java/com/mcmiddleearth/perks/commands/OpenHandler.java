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
import com.mcmiddleearth.perks.permissions.PermissionData;
import com.mcmiddleearth.pluginutil.NumericUtil;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Eriol_Eandur
 */
public class OpenHandler extends PerksCommandHandler {

    public OpenHandler(String... permissionNodes) {
        super(1,false,null,permissionNodes);
    }
    
    @Override
    public String getShortDescription(String cmd) {
        if(cmd.equalsIgnoreCase("open"))
            return ": "+PerksPlugin.getMessageUtil().INFO+"Gives a free perk to all players.";
        else 
            return ": "+PerksPlugin.getMessageUtil().INFO+"Removes a free perk.";
    }
    
    @Override
    public String getUsageDescription(String cmd) {
        if(cmd.equalsIgnoreCase("open"))
            return "<perk>[duration]: Gives <perk> to all players for [duration] minutes. "
                    +"Without a specified duration the free perk will last for 10 minutes.";
        else 
            return "<perk>: Removes the free <perk>. Players who got the perk for other reasons will still have it.";
    }
    
    @Override
    protected void execute(CommandSender cs, String cmd, String... args) {
        int duration=10;
        if(args.length>1 && NumericUtil.isInt(args[1])) {
            duration = NumericUtil.getInt(args[1]);
        }
        for(Perk search: PerkManager.getPerks()) {
            if(search.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                if(cmd.equals("open")) {
                    PermissionData.enableFreePerk(search, duration);
                    PerksPlugin.getMessageUtil().sendInfoMessage(cs, search.getName()
                            +" perk is now open for everyone for up to "+duration+" minutes."
                            +this.disabledWarning());
                    return;
                } else {
                    PermissionData.disableFreePerk(search);
                    search.check();
                    PerksPlugin.getMessageUtil().sendInfoMessage(cs, search.getName()
                            +" perk is no longer open for everyone.");
                    return;
                }
            }
        }
        sentInvalidArgumentMessage(cs);
    }
    
}
