package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Particle;

public class SoonboCommandExecutor implements Listener, CommandExecutor {

    private final DungPlugin plugin; // 메인 플러그인 인스턴스에 대한 참조
    private boolean soonboEnabled = false;

    public SoonboCommandExecutor(DungPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (soonboEnabled) {
            Player player = event.getPlayer();

            if (event.getItem() != null && isSword(event.getItem().getType()) && event.getAction().toString().contains("RIGHT")) {
                Location playerLocation = player.getLocation();
                Entity closestEntity = findClosestEntity(playerLocation, 10, player);

                if (closestEntity != null) {
                    Location newLocation = closestEntity.getLocation().clone().subtract(closestEntity.getLocation().getDirection().multiply(1));
                    player.teleport(newLocation);
                    player.sendMessage("순보!");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.5f, 0.1f);
                    player.getWorld().spawnParticle(Particle.CLOUD, newLocation, 100, 0.1, 1, 0.1, 1);
                } else {
                    player.sendMessage("주변에 엔티티가 없습니다.");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.5f);
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("soonbo")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                soonboEnabled = !soonboEnabled;

                if (soonboEnabled) {
                    player.sendMessage("순보 활성화!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
                } else {
                    player.sendMessage("순보 비활성화!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
                }

                return true;
            } else {
                sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
                return false;
            }
        }
        return false;
    }

    private Entity findClosestEntity(Location location, double maxDistance, Player player) {
        Entity closestEntity = null;
        double closestDistanceSquared = Double.MAX_VALUE;

        for (Entity entity : location.getWorld().getEntities()) {
            if (!entity.equals(player) && entity.getType() != EntityType.ARMOR_STAND) {
                double distanceSquared = entity.getLocation().distanceSquared(location);

                if (distanceSquared <= maxDistance * maxDistance && distanceSquared < closestDistanceSquared) {
                    closestEntity = entity;
                    closestDistanceSquared = distanceSquared;
                }
            }
        }

        return closestEntity;
    }

    // 검의 종류를 확인하는 메서드 추가
    private boolean isSword(Material material) {
        switch (material) {
            case IRON_SWORD:
            case DIAMOND_SWORD:
            case GOLDEN_SWORD:
            case STONE_SWORD:
            case WOODEN_SWORD:
            case NETHERITE_SWORD:
                return true;
            default:
                return false;
        }
    }
}
