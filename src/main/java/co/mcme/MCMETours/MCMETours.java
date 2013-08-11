package co.mcme.MCMETours;

import static co.mcme.MCMETours.CommandMethods.group;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
/**
*
* @author dags_ <dags@dags.me>
*/
public class MCMETours extends JavaPlugin {

	static boolean tpaon;
	static boolean tpon;
	static boolean broadcast;
	static boolean chaton;
	static boolean logchat;
	static String chatPrefix;
	static String chatSuffix;
	static ChatColor userChatColor;
	static ChatColor rangerChatColor;

	@Override
	public void onEnable() {
		setupConfig();
		dependencies();
		registerEvents();
	}

	@Override
	public void onDisable() {
		group.clear();
	}

	public void setupConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		tpaon = getConfig().getBoolean("Settings.AllowTpAll");
		tpon = getConfig().getBoolean("Settings.AllowTp");
		broadcast = getConfig().getBoolean("Settings.BroadcastTour");
		chaton = getConfig().getBoolean("Settings.TourChat");
		logchat = getConfig().getBoolean("Settings.LogTourChat");

		userChatColor = ChatColor.valueOf(getConfig().getString(
				"ChatSettings.UserChatColor"));
		rangerChatColor = ChatColor.valueOf(getConfig().getString(
				"ChatSettings.RangerChatColor"));
	}
	
	public void registerEvents() {
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(), this);
		if (chaton) {
			pm.registerEvents(new ChatListener(), this);
		}
		getCommand("tour").setExecutor(new TourCommands());

		getCommand("tourtpa").setExecutor(new TourCommands());
		getCommand("tourtp").setExecutor(new TourCommands());
		getCommand("ttpa").setExecutor(new TourCommands());
		getCommand("ttp").setExecutor(new TourCommands());
	}
	
	public void dependencies(){
		Plugin dl = getServer().getPluginManager().getPlugin("ChatSuite");
        if (dl != null) {
            System.out.println("Found ChatSuite, TourChat is possibru!");
        } else {
            System.out.println("Did not find ChatSuite, disabling TourChat!");
            chaton = false;
        }
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if ((sender.hasPermission("MCMETours.Admin"))) {
			if ((label.equalsIgnoreCase("tours"))) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.GRAY
							+ "/tours [reset], [set], " + "[broadcast], [info]");
					return true;
				}
				if (args[0].equalsIgnoreCase("reset")) {
					for (Player players : group.keySet()) {
						players.sendMessage(ChatColor.RED
								+ "All tours stopped by an Admin!");
					}
					group.clear();
					sender.sendMessage(ChatColor.RED + "MCMETours Reset!");
					return true;
				}
				if (args[0].equalsIgnoreCase("broadcast")) {
					if (broadcast) {
						this.getConfig().set("Settings.BroadcastTour", false);
						this.saveConfig();
						broadcast = false;
						sender.sendMessage(ChatColor.RED + "Broadcasting "
								+ ChatColor.GRAY + "disabled!");
						return true;
					} else {
						this.getConfig().set("Settings.BroadcastTour", true);
						this.saveConfig();
						broadcast = true;
						sender.sendMessage(ChatColor.RED + "Broadcasting "
								+ ChatColor.GRAY + "enabled!");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("info")) {
					sender.sendMessage(ChatColor.RED + "AllowTPA: "
							+ ChatColor.GRAY + tpaon);
					sender.sendMessage(ChatColor.RED + "AllowTP: "
							+ ChatColor.GRAY + tpon);
					sender.sendMessage(ChatColor.RED + "BroadcastTour: "
							+ ChatColor.GRAY + broadcast);
					return true;
				}
				if (args[0].equalsIgnoreCase("debug")) {
					sender.sendMessage(ChatColor.RED + "Players/Groups: "
							+ ChatColor.GRAY + group.entrySet());
					return true;
				}
			}
		}
		return false;
	}
}