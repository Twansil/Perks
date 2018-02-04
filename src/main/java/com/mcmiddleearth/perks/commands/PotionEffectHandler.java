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
import com.mcmiddleearth.perks.perks.PotionEffectPerk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class PotionEffectHandler extends PerksCommandHandler {

    public PotionEffectHandler(Perk perk, String... permissionNodes) {
        super(0,true,perk,permissionNodes);
    }
    
    @Override
    public String getShortDescription(String cmd) {
        return ": "+PerksPlugin.getMessageUtil().INFO+"Gives you "
                  +((PotionEffectPerk)this.getPerk()).getItemName()+".";
    }
    
    @Override
    public String getUsageDescription(String cmd) {
        return getShortDescription(cmd)+" Hold in hand an right-click to activate and deactivate.";
    }
    
    @Override
    protected void execute(CommandSender cs, String cmd, String... args) {
        Player player = (Player)cs;
        Perk perk = PerkManager.forName(cmd);
        if (!(perk instanceof PotionEffectPerk)) {
            PerksPlugin.getMessageUtil().sendErrorMessage(cs, "No matching perk found!");
            return;
        }
        ((PotionEffectPerk)perk).giveItem(player);
        PerksPlugin.getMessageUtil().sendInfoMessage(cs, "Have fun with your "
                                                         +((PotionEffectPerk)this.getPerk()).getItemName()+"!");
    }
    
}
