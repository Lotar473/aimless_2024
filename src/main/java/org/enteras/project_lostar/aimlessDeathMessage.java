package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.ChatColor;

public class aimlessDeathMessage implements Listener {

    public aimlessDeathMessage() {
        Bukkit.getPluginManager().registerEvents(this, DungPlugin.getPlugin(DungPlugin.class));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String deathMessage = ChatColor.RED + "사람이 죽었다.";

        event.setDeathMessage(deathMessage);
    }
}
