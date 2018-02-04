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
import com.mcmiddleearth.perks.commands.PerksCommandHandler;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

/**
 *
 * @author Eriol_Eandur
 */
public class Perk {
    
    @Getter
    private final String name;
    
    @Getter
    private PerksCommandHandler handler;
    
    @Getter
    private Listener listener;
    
    @Getter
    private String[] commands;
    
    public Perk(String name) {
        this.name = name;
    }
    
    public boolean isEnabled() {
        return PerksPlugin.getInstance().isPerkEnabled(this);
    }
    
    public void setListener(Listener listener) {
        this.listener = listener;
    }
    
    public void setCommandHandler(PerksCommandHandler handler, String... commands) {
        this.handler = handler;
        this.commands = commands;
    }
  
    public String getPermissionNode() {
        return "perks."+getName();
    }

    public void disable() {}
    
    public void check() {}
    
    public void enable() {}
    
    public void writeDefaultConfig(ConfigurationSection config) {}
}
