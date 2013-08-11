package co.mcme.MCMETours;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mcme.MCMETours.CommandMethods;
/**
*
* @author dags_ <dags@dags.me>
*/
public class TourCommands implements CommandExecutor {

	ChatColor error = ChatColor.GRAY;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String c,
			String[] a) {
		Player p = (Player) sender;

		if (!(sender instanceof Player)) {
			p.sendMessage(error + "This command can only be run by a player.");
			return true;
		} else {
			if (c.equalsIgnoreCase("tour")) {
				if (p.hasPermission("MCMETours.user")) {
					if (a.length == 0) {
						CommandMethods.tourInfo(p);
						return true;
					} else {
						if (a[0].equalsIgnoreCase("request")) {
							CommandMethods.tourRequest(p);
							return true;
						}
						if (a[0].equalsIgnoreCase("join")) {
							if (a.length == 1) {
								p.sendMessage(error
										+ "Not enough arguments! /tour join <name>");
								return true;
							} else {
								CommandMethods.tourJoin(p, a[1]);
								return true;
							}
						}
						if (a[0].equalsIgnoreCase("leave")) {
							CommandMethods.tourLeave(p);
							return true;
						}
						if (a[0].equalsIgnoreCase("help")) {
							CommandMethods.tourHelp(p);
							return true;
						}
					}
				}
				if (p.hasPermission("MCMETours.ranger")) {
					if (a[0].equalsIgnoreCase("start")) {
						CommandMethods.tourStart(p);
						return true;
					}
					if (a[0].equalsIgnoreCase("stop")) {
						CommandMethods.tourStop(p);
						return true;
					}
					if (a[0].equalsIgnoreCase("list")) {
						CommandMethods.tourList(p);
						return true;
					}
				} else {
					p.sendMessage(error + "Sorry, you are not a Ranger!");
					return true;
				}
			}
			if (c.equalsIgnoreCase("tourtp") || c.equalsIgnoreCase("ttp")) {
				if (p.hasPermission("MCMETours.ranger")) {
					if (a.length == 1) {
						CommandMethods.tourTp(p, a[0]);
						return true;
					} else {
						p.sendMessage(error + "Incorrect use! /tourtp <player>");
						return true;
					}
				} else {
					p.sendMessage(error + "Sorry, you are not a Ranger!");
					return true;
				}
			}
			if (c.equalsIgnoreCase("tourtpa") || c.equalsIgnoreCase("ttpa")) {
				if (p.hasPermission("MCMETours.ranger")) {
					if (a.length == 0) {
						CommandMethods.tourTpa(p);
						return true;
					}
				} else {
					p.sendMessage(error + "Sorry, you are not a Ranger!");
					return true;
				}
			}
		}
		return false;
	}

}
