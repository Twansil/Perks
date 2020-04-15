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
import com.mcmiddleearth.perks.listeners.NameTagListener;
import com.mcmiddleearth.perks.permissions.PermissionData;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 *
 * @author Eriol_Eandur
 */
public class NameTagPerk extends Perk {
    
    private final ChatColor nameTagColor = ChatColor.valueOf(
                                           PerksPlugin.getPerkString(this.getName(),
                                                                      "donorTagColor",
                                                                      "YELLOW"));
    
    private Scoreboard board;
    private Objective objective;
    private Team team;
    private final String teamName = "NameTagPerkTeam";
    
    public NameTagPerk() {
        super("name");
        board = Bukkit.getScoreboardManager().getMainScoreboard();

        enable();
        setListener(new NameTagListener(this));
        
    }
  
    public void setNameTag(Player player, boolean color) {
        if(team!=null) {
            if(color) {
                team.addPlayer(player);
            } else {
                team.removePlayer(player);
            }
        }
    }
    
    @Override
    public void disable() {
        for(Player player: Bukkit.getOnlinePlayers()) {
            setNameTag(player,false);
        }
        if(team!=null) {
            team.unregister();
        }
        team=null;
    }

    @Override
    public void check() {
        for(Player player: Bukkit.getOnlinePlayers()) {
            if(PermissionData.isAllowed(player, this)) {
                setNameTag(player,true);
            } else {
                setNameTag(player,false);
            }
        }
    }
    
    @Override
    public final void enable() {
        team = board.getTeam(teamName);
        if(team==null) {
            team = board.registerNewTeam(teamName);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
            team.setColor(nameTagColor);
        }
        check();
    }
    
    @Override
    public void writeDefaultConfig(ConfigurationSection config) {
        config.set("donorTagColor", nameTagColor.name());
    }
}
    