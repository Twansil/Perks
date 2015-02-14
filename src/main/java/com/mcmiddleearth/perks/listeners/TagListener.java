/*
 * This file is part of Perks.
 * 
 * Perks is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Perks is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Perks.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
package com.mcmiddleearth.perks.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.mcmiddleearth.perks.MCMEPerks;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class TagListener implements Listener{
	
	static ScoreboardManager manager;
	static Scoreboard board;
	static Objective objective;
	static Team donator;
	
	public static void setBoard(){
		manager = Bukkit.getScoreboardManager();
    	board = manager.getMainScoreboard();
    	clearBoard();
    	donator = board.registerNewTeam("Donators");
    	donator.setPrefix(MCMEPerks.donarTag.toString());
	}
    
    @EventHandler (priority = EventPriority.LOWEST)
    public void playerJoin(PlayerJoinEvent event){
    	Player p = event.getPlayer();
    	if(p.hasPermission("perks.tag")){
    		donator.addPlayer(p);
    		if(!p.getDisplayName().startsWith("§")){
    			String name = p.getName();
    			if(name.length()>=15){
    				name = name.substring(0, 14);
    			}
    			p.setPlayerListName("§f"+name);
    		}
    	}
    }
    
    @EventHandler (priority = EventPriority.NORMAL)
    public void playerQuit(PlayerQuitEvent event){
    	Player p = event.getPlayer();
    	if(donator.hasPlayer(p)){
    		donator.removePlayer(p);
    	}
    }
    
    @EventHandler (priority = EventPriority.NORMAL)
    public void playerKicked(PlayerKickEvent event){
    	Player p = event.getPlayer();
    	if(donator.hasPlayer(p)){
    		donator.removePlayer(p);
    	}
    }
    
    public static void clearBoard(){
    	if(!board.getTeams().isEmpty()){
    		for(Team t : board.getTeams()){
        		t.unregister();
        	}
    	}
    }
}
