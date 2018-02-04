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
import com.mcmiddleearth.perks.perks.ItemPerk;
import com.mcmiddleearth.perks.perks.Perk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class ItemHandler extends PerksCommandHandler {

    public ItemHandler(Perk perk, String... permissionNodes) {
        super(0,true,perk,permissionNodes);
    }
    
    @Override
    public String getShortDescription(String cmd) {
        /*if(cmd.startsWith("a")||cmd.startsWith("e")||cmd.startsWith("i")||cmd.startsWith("o")||cmd.startsWith("u")) {
            add = "n";
        }*/
        return ": "+PerksPlugin.getMessageUtil().INFO+"Gives you "
                   +((ItemPerk)this.getPerk()).getItemName()+".";
    }
    
    @Override
    public String getUsageDescription(String cmd) {
        return getShortDescription(cmd)+" There is nothing special about that item except of the name. "
                +"You can also get it from creative inventory.";
    }
    
    @Override
    protected void execute(CommandSender cs, String cmd, String... args) {
        Player player = (Player)cs;
        if (((ItemPerk)getPerk()).hasItem(player)) {
            PerksPlugin.getMessageUtil().sendErrorMessage(cs, "You already have this item: "+getPerk().getName());
            return;
        }
        ((ItemPerk)getPerk()).giveItem(player);
        PerksPlugin.getMessageUtil().sendInfoMessage(cs, "Have fun with your "
                                                       +((ItemPerk)this.getPerk()).getItemName());
    }
    
}
