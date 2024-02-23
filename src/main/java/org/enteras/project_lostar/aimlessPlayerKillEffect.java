package org.enteras.project_lostar;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class aimlessPlayerKillEffect implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player killer = event.getEntity().getKiller();
            if (event.getEntityType() == EntityType.PLAYER) {
                Player victim = (Player) event.getEntity();
                playSoulParticles(victim.getLocation());
                playCustomSound(killer);
            }
        }
    }

    private void playSoulParticles(Location location) {
        location.getWorld().spawnParticle(Particle.SOUL, location, 222, 0, 1.5, 0, 0.1);
    }

    private void playCustomSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
    }

}
