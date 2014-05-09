package co.mcme.perks.commands;

import static co.mcme.perks.MCMEPerks.perks;
import static co.mcme.perks.MCMEPerks.prim;
import static co.mcme.perks.MCMEPerks.scd;
import static co.mcme.perks.MCMEPerks.toggle;
import static co.mcme.perks.MCMEPerks.walkSpeed;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import co.mcme.perks.MCMEPerks;
import co.mcme.perks.utils.HorseUtil;
import co.mcme.perks.utils.ItemUtil;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class CommandMethods {

	public static void giveHorse(Player p) {
		if (p.hasPermission("perks.mount")) {
			if (p.isInsideVehicle()) {
				p.sendMessage(scd + "You are already on a horse!");
				return;
			} else {
				HorseUtil.giveHorse(p);
				return;
			}
		} else {
			p.sendMessage(scd + "Sorry you haven't unlocked this perk!");
			return;
		}
	}

	public static int clearHorses(Player p) {
		World w = p.getWorld();
		int i = 0;
		for (Entity e : w.getEntities()) {
			if (e.getType().equals(EntityType.HORSE)) {
				e.remove();
				i++;
			}
		}
		return i;
	}

	public static void giveItem(Player p, String perk) {
		String str = perk.toLowerCase();
		if (perks.contains(str)) {
			if (p.hasPermission("perks.item." + str)) {
				ItemUtil.giveItem(p, str);
				return;
			} else {
				p.sendMessage(scd + "Sorry, you haven't unlocked that perk!");
				return;
			}
		} else {
			p.sendMessage(prim + "/perk " + perks.toString());
			return;
		}
	}
	
	public static void playerWalk(Player p){
		if(p.hasPermission("perks.walk")){
			if(p.getWalkSpeed()!=(float) 0.2){
				p.setWalkSpeed((float) 0.2);
				p.sendMessage(scd + "Walk-speed disabled!");
				return;
			} else {
				p.setWalkSpeed(walkSpeed);
				p.sendMessage(prim + "Walk-speed enabled!");
				return;
			}
		} else {
			p.sendMessage(scd + "Sorry you don't have permission to use that!");
			return;
		}
	}

	public static void saveToggle() {
		MCMEPerks.inst().getConfig().set("Settings.Enabled", toggle);
		MCMEPerks.inst().saveConfig();
	}
}
