/*
 * Copyright (c) 2015 dags_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
