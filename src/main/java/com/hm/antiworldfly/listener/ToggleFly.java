package com.hm.antiworldfly.listener;

import java.util.logging.Level;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import com.hm.antiworldfly.AntiWorldFly;
import com.hm.mcshared.particle.FancyMessageSender;

/**
 * Class to disable flying if a player manages to toggle flying.
 * 
 * @author Pyves
 * @maintainer Sidpatchy
 */
public class ToggleFly implements Listener {

	private AntiWorldFly plugin;

	public ToggleFly(AntiWorldFly awf) {
		this.plugin = awf;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerCommandPreprocessEvent(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();
		String playerWorld = player.getWorld().getName().toLowerCase();

		if (player.hasPermission("antiworldfly.fly." + playerWorld)) return;

		if (!this.plugin.isAntiFlyCreative() && player.getGameMode() == GameMode.CREATIVE) return;
		if (player.getGameMode() == GameMode.SPECTATOR) return;

		if (!plugin.getAntiFlyWorlds().contains(playerWorld)) return;

		// Disable flying.
		player.setAllowFlight(false);
		player.getPlayer().setFlying(false);
		event.setCancelled(true);

		if (plugin.isChatMessage()) {
			player.sendMessage(plugin.getChatHeader() + plugin.getPluginLang().getString("fly-disabled-chat",
					"Flying is disabled in this world."));
		}

		if (plugin.isTitleMessage()) {
			try {
				FancyMessageSender.sendTitle(player,
						plugin.getPluginLang().getString("fly-disabled-title", "&9AntiWorldFly"),
						plugin.getPluginLang().getString("fly-disabled-subtitle", "Flying is disabled in this world."));
			} catch (Exception e) {
				plugin.getLogger().log(Level.SEVERE, "Errors while trying to display flying disabled title: ", e);
			}
		}
	}
}
