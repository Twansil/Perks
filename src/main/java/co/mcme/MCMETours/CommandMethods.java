package co.mcme.MCMETours;

import static co.mcme.MCMETours.MCMETours.broadcast;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
/**
*
* @author dags_ <dags@dags.me>
*/
public class CommandMethods {

	static ChatColor error = ChatColor.GRAY;
	static ChatColor leave = ChatColor.DARK_GRAY;
	static ChatColor neutral = ChatColor.YELLOW;
	static ChatColor positive = ChatColor.GREEN;
	static ChatColor negative = ChatColor.RED;
	static ChatColor ranger = ChatColor.DARK_AQUA;
	static ChatColor tp = ChatColor.LIGHT_PURPLE;

	public static HashMap<Player, String> group = new HashMap<Player, String>();

	public static void tourInfo(Player p) {
		if (group.isEmpty()) {
			p.sendMessage(neutral + "There are no tours running right now.");
			p.sendMessage(positive + "Online Rangers: ");
			for (Player q : Bukkit.getOnlinePlayers()) {
				if (q.hasPermission("MCMETours.ranger")) {
					p.sendMessage(ranger + q.getName());
				}
			}
		} else {
			p.sendMessage(positive + "Currently running tours:");
			for (Player q : group.keySet()) {
				if (group.get(q).equalsIgnoreCase(q.getName().toLowerCase())) {
					p.sendMessage(ranger + q.getName());
				}
			}
		}
	}

	public static void tourRequest(Player p) {
		p.sendMessage(neutral + "Request sent!");
		for (Player q : Bukkit.getOnlinePlayers()) {
			if (q.hasPermission("MCMETours.ranger")) {
				q.sendMessage(positive + p.getName() + neutral
						+ " requests a tour!");
			}
		}
	}

	public static void tourJoin(Player p, String s) {
		if (group.isEmpty()) {
			p.sendMessage(error + "There are no tours running right now!");
			return;
		}
		if (isTouring(p)) {
			p.sendMessage(error + "You are currently leading a tour!");
			return;
		} else {
			if (getPlayer(s,p) != ("null")) {
				Player q = Bukkit.getPlayerExact(getPlayer(s,p));
				if(group.containsKey(p) && group.get(p).equals(q.getName())){
					p.sendMessage(error+"You are already part of this tour!");
					return;
				}else{
					if (group.get(q).equals(q.getName())) {
						for (Player r : group.keySet()) {
							if (group.get(r).equals(q.getName())) {
								r.sendMessage(ChatColor.GREEN
										+ "Everybody welcome " + ChatColor.YELLOW
										+ p.getName() + ChatColor.GREEN
										+ " to the tour!");
							}
						}
						p.sendMessage(positive + "Joined!");
						group.put(p, q.getName());
					} else {
						p.sendMessage(error + "No tours found by that name!");
					}
					return;
				}
			} else {
				p.sendMessage(error + "No tours found by that name!");
				return;
			}
		}
	}

	public static void tourLeave(Player p) {
		if (group.containsKey(p)) {
			if (group.containsValue(p.getName())) {
				p.sendMessage(error + "You are currently leading a tour!");
				return;
			} else {
				for (Player q : Bukkit.getOnlinePlayers()) {
					if (group.get(q) == group.get(p) && p != q) {
						q.sendMessage(leave + p.getName() + " left the tour.");
					}
				}
				group.remove(p);
				p.sendMessage(leave + "You left the tour.");
				return;
			}
		} else {
			p.sendMessage(error + "You are not currently in a tour!");
			return;
		}
	}

	public static void tourHelp(Player p) {
		p.sendMessage(error + "/tour - check for tour information");
		p.sendMessage(error + "/tour request - submit a request for a tour");
		p.sendMessage(error + "/tour join <RangerName> - join Ranger's tour");
		p.sendMessage(error + "/tour leave - leave current tour");
		if (p.hasPermission("MCMETours.ranger")) {
			p.sendMessage(error + "/tour start - host a tour");
			p.sendMessage(error + "/tour stop - stop your tour");
			p.sendMessage(error + "/tour list - list users on your tour");
			p.sendMessage(error
					+ "/tourtp <playername> - tp player on your tour to you");
			p.sendMessage(error
					+ "/tourtpa - tp all players on your tour to you");
		}
	}

	public static void tourStart(Player p) {
		if (isTouring(p)) {
			p.sendMessage(error + "You are already leading a tour!");
			return;
		} else {
			group.put(p, p.getName());
			p.sendMessage(positive + "Tour started!");
			if (broadcast) {
				Bukkit.broadcastMessage(positive + "Tour starting soon with "
						+ ranger + p.getName());
				Bukkit.broadcastMessage(positive + "Enter '" + neutral
						+ "/tour join " + p.getName() + positive
						+ "' to join in!");
			}
			return;
		}
	}

	public static void tourStop(Player p) {
		if (isTouring(p)) {
			for (Player q : Bukkit.getOnlinePlayers()) {
				if (group.get(q) == p.getName()) {
					group.remove(q);
					if (q == p) {
						p.sendMessage(negative + "Tour stopped");
					} else {
						q.sendMessage(negative
								+ "The tour has ended. See you next time!");
					}
				}
			}
		} else {
			p.sendMessage(error + "You are not running a tour!");
		}
	}

	public static void tourList(Player p) {
		if (isTouring(p)) {
			ArrayList<Object> tourgroup = new ArrayList<Object>();
			for (Player q : group.keySet()) {
				if (group.get(q).contains(p.getName())) {
					tourgroup.add(q.getName());
				}
			}
			p.sendMessage(neutral + tourgroup.toString());
		} else {
			p.sendMessage(error + "You are not running a tour!");
		}
	}

	public static void tourTp(Player p, String s) {
		if (!getPlayer(s,p).equals("null")) {
			if (group.containsKey(p)) {
				Player q = Bukkit.getPlayerExact(getPlayer(s,p));
				if (group.get(q) == group.get(p) && group.get(q) != q.getName()) {
					Location l = p.getLocation();
					q.teleport(l);
					q.sendMessage(tp + "You were teleported to your guide!");
					p.sendMessage(tp + "Teleported!");
				}
			} else {
				p.sendMessage(error + "You are not part of a tour!");
				return;
			}
		} else {
			p.sendMessage(error + "User not on your tour!");
			return;
		}
	}

	public static void tourTpa(Player p) {
		if (isTouring(p)) {
			Location l = p.getLocation();
			for (Player q : Bukkit.getOnlinePlayers()) {
				if (group.get(q) == p.getName() && q != p) {
					q.teleport(l);
					q.sendMessage(tp + "You were teleported to your guide!");
				}
			}
			p.sendMessage(tp + "Teleported!");
		} else {
			p.sendMessage(error + "You are not running a tour!");
		}
	}

	private static String getPlayer(String s, Player p) {
		String q = "null";
		for (Player r : Bukkit.getOnlinePlayers()) {
			if (r.getName().toLowerCase().contains(s.toLowerCase())
					&& !r.equals(p)) {
				q = r.getName();
			}
		}
		return q;
	}

	private static boolean isTouring(Player p) {
		if (group.containsValue(p.getName())) {
			return true;
		} else {
			return false;
		}
	}

}
