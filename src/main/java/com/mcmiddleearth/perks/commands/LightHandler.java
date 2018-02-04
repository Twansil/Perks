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
import com.mcmiddleearth.perks.perks.WizardLightPerk;
import com.mcmiddleearth.pluginutil.NumericUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class LightHandler extends PerksCommandHandler {

    public LightHandler(Perk perk, String... permissionNodes) {
        super(0,true,perk,permissionNodes);
    }
    
    @Override
    public String getShortDescription(String cmd) {
        return ": "+PerksPlugin.getMessageUtil().INFO+"Gives you a wizard's light.";
    }
    
    @Override
    public String getUsageDescription(String cmd) {
        return "[intensity]: The number [intensity] specifies the radius of the lighted area. "
                +"With a "+PerksPlugin.getMessageUtil().INFO+WizardLightPerk.getItem()+PerksPlugin.getMessageUtil().HIGHLIGHT
                +" in hand you can steer your light by right and left clicking.";
    }
    
    @Override
    protected void execute(CommandSender cs, String cmd, String... args) {
        Player player = (Player) cs;
        if(args.length>0) {
            switch (args[0]) {
                case "a":
                    WizardLightPerk.setSpeedFactor(Double.parseDouble(args[1]));
                    break;
                case "b":
                    WizardLightPerk.setMaxSpeed(Double.parseDouble(args[1]));
                    break;
            }
        }
        if(!WizardLightPerk.hasLight(player)) {
            WizardLightPerk.summonLight(player);
            if(args.length>0 && NumericUtil.isInt(args[0])) {
                WizardLightPerk.setIntensity(player, NumericUtil.getInt(args[0]));
            }
            PerksPlugin.getMessageUtil().sendInfoMessage(player, ""+ChatColor.GOLD+ChatColor.BOLD+"Lumos."
                                                                   +ChatColor.AQUA+" Err, wrong book...");
        } else {
            if(args.length>0 && NumericUtil.isInt(args[0])) {
                int intensity = NumericUtil.getInt(args[0]);
                if(intensity>0 && intensity <=WizardLightPerk.getMaxIntensity()) {
                    WizardLightPerk.setIntensity(player, NumericUtil.getInt(args[0]));
                    PerksPlugin.getMessageUtil().sendInfoMessage(player, "Intensity set to "+intensity+".");
                } else {
                    PerksPlugin.getMessageUtil().sendErrorMessage(player, "You are not allowed to use intensity "+intensity+".");
                    
                }
            } else {
                WizardLightPerk.unsummonLight(player);
                PerksPlugin.getMessageUtil().sendInfoMessage(player, "Wizard's Light unsummoned.");
            }
        }
    }
    
}
