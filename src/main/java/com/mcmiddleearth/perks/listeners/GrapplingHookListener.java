package com.mcmiddleearth.perks.listeners;

import static com.mcmiddleearth.perks.MCMEPerks.scd;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * 
 * @author meggawatts <meggawatts@mcme.co>
 */
public class GrapplingHookListener implements Listener {

	Map<String, Boolean> lineIsOut = new HashMap<String, Boolean>();
	Map<String, Fish> hookDest = new HashMap<String, Fish>();
	Map<String, PlayerFishEvent> fishEventMap = new HashMap<String, PlayerFishEvent>();

	// itemName = ChatColor.GOLD + "Grappling Hook Perk";
	// lore.add(ChatColor.DARK_PURPLE + owner);
	@EventHandler
	public void throwHook(ProjectileLaunchEvent event) {
		if ((event.getEntity() instanceof Fish)) {
			Fish hook = (Fish) event.getEntity();
			if ((hook.getShooter() != null)
					&& ((hook.getShooter() instanceof Player))) {
				Player player = (Player) hook.getShooter();
				ItemStack iih = new ItemStack(player.getItemInHand());

				ItemMeta im = iih.getItemMeta();

				if (im.getDisplayName() == null || im.getLore() == null) {
					return;
				}
				if (im.getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Grappling Hook Perk") 
						&& im.getLore().toString().contains(player.getName()) 
						&& player.hasPermission("perks.item.hookshot")) {
					setLineOut(player, Boolean.valueOf(true));
					this.hookDest.put(player.getName(), hook);
				} else {
					player.sendMessage(scd + "Sorry, you cannot use that perk item!");
					player.getInventory().remove(player.getItemInHand());
				}
			}
		}
	}

	@EventHandler
	public void fishEvent(PlayerFishEvent event) {
		Player player = event.getPlayer();
		ItemStack iih = new ItemStack(player.getItemInHand());

		ItemMeta im = iih.getItemMeta();

		if (im.getDisplayName() == null || im.getLore() == null) {
			return;
		}
		if (!im.getDisplayName().equalsIgnoreCase(
				ChatColor.GOLD + "Grappling Hook Perk")
				|| !im.getLore().toString().contains(player.getName())) {
			return;
		}

		if (event.getState() == PlayerFishEvent.State.IN_GROUND) {
			if (this.hookDest.get(player.getName()) == null) {
				return;
			}
			Location loc = ((Fish) this.hookDest.get(player.getName()))
					.getLocation();
			loc.setPitch(player.getLocation().getPitch());
			loc.setYaw(player.getLocation().getYaw());
			player.getWorld().playSound(loc, Sound.MAGMACUBE_JUMP, 10.0F, 1.0F);
			player.teleport(loc);
		} else if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
			event.getCaught().teleport(player.getLocation());
		}
		if (event.getState() != PlayerFishEvent.State.FISHING) {
			setLineOut(player, Boolean.valueOf(false));
			setFishEvent(player, null);
		} else {
			setFishEvent(player, event);
		}
	}

	public void setLineOut(Player player, Boolean b) {
		this.lineIsOut.put(player.getName(), b);
	}

	public Boolean getLineOut(Player player) {
		if (this.lineIsOut.containsKey(player.getName())) {
			return (Boolean) this.lineIsOut.get(player.getName());
		}
		return Boolean.valueOf(false);
	}

	public void setFishEvent(Player player, PlayerFishEvent e) {
		this.fishEventMap.put(player.getName(), e);
	}

	public PlayerFishEvent getFishEvent(Player player) {
		if (this.fishEventMap.containsKey(player.getName())) {
			return (PlayerFishEvent) this.fishEventMap.get(player.getName());
		}
		return null;
	}
}
