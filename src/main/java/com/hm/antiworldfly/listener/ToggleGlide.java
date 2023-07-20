package com.hm.antiworldfly.listener;

import com.hm.antiworldfly.AntiWorldFly;
import com.hm.mcshared.particle.FancyMessageSender;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

import java.util.logging.Level;

/**
 * Class to block the player from using the Elytra in blocked worlds
 *
 * @author Sidpatchy
 */
public class ToggleGlide implements Listener {

    private final AntiWorldFly plugin;

    public ToggleGlide(AntiWorldFly awf) {
        this.plugin = awf;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {

        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;

        if (!plugin.isElytraDisabled()) return;

        String playerWorld = player.getWorld().getName();
        if (player.hasPermission("antiworldfly.elytra." + playerWorld)) return;
        if (!plugin.isAntiFlyCreative() && player.getGameMode() == GameMode.CREATIVE) return;

        if (!plugin.getAntiFlyWorlds().contains(playerWorld)) return;

        // Disable elytra
        event.setCancelled(true);

        if (plugin.isChatMessage()) {
            player.sendMessage(plugin.getChatHeader() + plugin.getPluginLang().getString("elytra-disabled-subtitle",
                    "Elytras are disabled in this world."));
        }

        if (plugin.isTitleMessage()) {
            try {FancyMessageSender.sendTitle(player,
                    plugin.getPluginLang().getString("fly-disabled-title", "&9AntiWorldFly"),
                    plugin.getPluginLang().getString("elytra-disabled-subtitle", "Elytras are disabled in this world."));
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Errors while trying to display flying disabled title: ", e);
            }
        }

    }
}
