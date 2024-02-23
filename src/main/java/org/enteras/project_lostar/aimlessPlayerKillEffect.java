package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class aimlessPlayerKillEffect implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player killer = event.getEntity().getKiller();
            if (event.getEntityType() == EntityType.PLAYER) {
                Player victim = (Player) event.getEntity();
                summonSilentLightning(victim);
                playCustomSound(killer);
            }
        }
    }

    private void summonSilentLightning(Player player) {
        player.getWorld().strikeLightningEffect(player.getLocation());
    }

    private void playCustomSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
    }
}
