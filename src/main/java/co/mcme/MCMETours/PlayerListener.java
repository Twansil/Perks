package co.mcme.MCMETours;

import static co.mcme.MCMETours.CommandMethods.group;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
/**
*
* @author dags_ <dags@dags.me>
*/
public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerJoin(PlayerJoinEvent event) {
		Player p = (Player) event.getPlayer();
		if (!(p.hasPlayedBefore())) {
			for (Player g : Bukkit.getOnlinePlayers()) {
				if (g.hasPermission("MCMETours.ranger")) {
					g.sendMessage(ChatColor.LIGHT_PURPLE + p.getName()
							+ ChatColor.DARK_PURPLE
							+ " is a newbie to the server, give 'em a hug!");
				}
			}
		}

		if (group.size() == 0) {
			p.sendMessage(ChatColor.YELLOW
					+ "There are no tours running right now.");
			p.sendMessage(ChatColor.YELLOW + "See " + ChatColor.GREEN
					+ "/tour help" + ChatColor.YELLOW + " for more info!");
		}
		if (group.size() > 0) {
			p.sendMessage(ChatColor.GREEN
					+ "There are 1 or more tours currently running!");
			p.sendMessage(ChatColor.YELLOW + "See " + ChatColor.GREEN
					+ "/tour help" + ChatColor.YELLOW + " for more info!");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (p.hasPermission("MCMETours.Ranger")) {
			RangerQuit(p);
		} else {
			if (group.containsKey(p)) {
				group.remove(p);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerKicked(PlayerKickEvent event) {
		Player p = event.getPlayer();
		if (p.hasPermission("MCMETours.Ranger")
				&& group.containsValue(p.getName().toLowerCase())) {
			RangerQuit(p);
		} else {
			if (group.containsKey(p)) {
				group.remove(p);
			}
		}
	}

	private void RangerQuit(Player p) {
		group.remove(p);
		for (Player q : Bukkit.getOnlinePlayers()) {
			if (group.containsKey(q) && group.get(q).contains(p.getName())
					&& p != q) {
				group.remove(q);
				q.sendMessage(ChatColor.RED
						+ "Your guide has left. The tour is now over!");
			}
		}
	}
}
