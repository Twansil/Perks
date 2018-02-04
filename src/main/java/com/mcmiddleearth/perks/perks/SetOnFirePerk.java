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
import com.mcmiddleearth.perks.commands.FireHandler;
import com.mcmiddleearth.perks.permissions.Permissions;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Eriol_Eandur
 */
public class SetOnFirePerk extends Perk {
    
    @Getter
    private final int defaultTicks=PerksPlugin.getPerkInt(this.getName(), "defaultTicks",200);

    @Getter
    private final int maxTicks=PerksPlugin.getPerkInt(this.getName(), "maxTicks",2000);
    
    public SetOnFirePerk() {
        super("fire");
        setCommandHandler(new FireHandler(this, Permissions.USER.getPermissionNode()),"fire");
    }
    
    @Override
    public void writeDefaultConfig(ConfigurationSection config) {
        config.set("defaultTicks", defaultTicks);
        config.set("maxTicks", maxTicks);
    }
  
}
    