package com.mcmiddleearth.perks.commands;

import com.mcmiddleearth.perks.PerksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class VideoTeamCommand implements CommandExecutor {

    private static Team team;
    private static final Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
    private static final String teamName = "videoteam";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if(team == null) return false;
        if(team.hasPlayer(player)) {
            team.removePlayer(player);
            sendShowNametagMessage(player);
        }
        else {
            team.addPlayer(player);
            sendActivateMessage(player);
        }
        return true;
    }

    public static void enableVideoTeam(){
        team = board.getTeam(teamName);
        if(team == null){
            team = board.registerNewTeam(teamName);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY,Team.OptionStatus.FOR_OTHER_TEAMS);
        }
    }

    public static void disableVideoTeam(){
        for(Player player : Bukkit.getOnlinePlayers()) team.removePlayer(player);
        if(team !=null) team.unregister();
        team = null;
    }

    private void sendActivateMessage(Player player){
        PerksPlugin.getMessageUtil().sendInfoMessage(player,"You are now hiding your nametag");
    }

    private void sendShowNametagMessage(Player player){
        PerksPlugin.getMessageUtil().sendInfoMessage(player,"You are now showing your nametag");
    }
}
