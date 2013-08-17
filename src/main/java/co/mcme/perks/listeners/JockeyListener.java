package co.mcme.perks.listeners;

import static co.mcme.perks.MCMEPerks.prim;
import static co.mcme.perks.MCMEPerks.scd;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JockeyListener implements Listener{
	
	public static void jockey(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if(p.isInsideVehicle()){
			p.getVehicle().eject();
			p.sendMessage(scd + "You have been ejected!");
		} else { 
			Entity ride = null;
			for (Entity e : event.getPlayer()
					.getNearbyEntities(3, 3, 3)) {
				if (e instanceof Player) {
					if (!e.isInsideVehicle()) {
						ride = e;
					}
				}
			}
			if (ride != null) {
				Player q = (Player) ride;
				if(!q.hasPermission("perks.antijockey")){
					ride.setPassenger(p);
					p.sendMessage(prim + "Now riding "+ scd + q.getName() + prim + "!");
				} else {
					p.sendMessage(scd + "You cannot ride that player!");
				}
			} else {
				p.sendMessage(scd + "There are no nearby players to ride!");
			}
		}
	}
	
	@EventHandler
	private void eject(PlayerInteractEntityEvent event){
		if(event.getRightClicked() instanceof Player
				&& event.getRightClicked().isInsideVehicle()){
			Player jockey = (Player) event.getRightClicked();
			if(jockey.getVehicle() instanceof Player){
				Player ride = (Player) jockey.getVehicle();
				if(ride.equals(event.getPlayer())){
					jockey.getVehicle().eject();
					jockey.sendMessage(scd + "You have been ejected!");
				}
			}
		}
	}
	
	@EventHandler
	public void riderQuit(PlayerQuitEvent event) {
		if (event.getPlayer().isInsideVehicle()) {
			if(event.getPlayer().getVehicle() instanceof Player){
				event.getPlayer().getVehicle().eject();
			}
		}
	}

	@EventHandler
	public void riderKick(PlayerKickEvent event) {
		if (event.getPlayer().isInsideVehicle()) {
			if(event.getPlayer().getVehicle() instanceof Player){
				event.getPlayer().getVehicle().eject();
			}
		}
	}

}
