package org.enteras.project_lostar;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class aimlessPreventingMultiKills implements Listener {

    private Map<UUID, Long> invulnerablePlayers = new HashMap<>();
    private final long invulnerableDuration = 5 * 60 * 1000; // 5분을 밀리초로 변환

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            // 플레이어가 다른 플레이어에 의해 사망한 경우
            giveInvulnerability(victim);
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();
            if (invulnerablePlayers.containsKey(victim.getUniqueId())) {
                // 플레이어가 무적 상태인 경우
                event.setCancelled(true); // 공격 취소
                long remainingTime = getRemainingInvulnerableTime(victim);

                // 분과 초 계산
                long minutes = remainingTime / 60000;
                long seconds = (remainingTime % 60000) / 1000;

                // 메시지 생성
                String timeMessage = String.format(ChatColor.RED + "%d분 %d초" + ChatColor.WHITE + "동안 무적입니다!", minutes, seconds);
                attacker.sendMessage(ChatColor.BOLD.toString() + ChatColor.RED + "연속 킬 방지! " + ChatColor.RESET + "이 플레이어는 " + timeMessage);
            } else if (invulnerablePlayers.containsKey(attacker.getUniqueId())) {
                // 만약 공격자가 무적인 경우 공격 취소
                event.setCancelled(true);
                attacker.sendMessage(ChatColor.RED + "무적 상태에서 다른 플레이어를 공격할 수 없습니다!");
            }
        }
    }


    private void giveInvulnerability(Player player) {
        invulnerablePlayers.put(player.getUniqueId(), System.currentTimeMillis() + invulnerableDuration);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(ChatColor.RED + "다른 플레이어에게 사망하여 5분의 무적시간이 주어졌습니다!");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        removeInvulnerability(player);
                        player.sendMessage(ChatColor.RED + "무적 상태가 해제되었습니다!");
                    }
                }.runTaskLater(DungPlugin.getPlugin(DungPlugin.class), invulnerableDuration);
            }
        }.runTaskLater(DungPlugin.getPlugin(DungPlugin.class), 1); // 1 tick delay
    }


    private void removeInvulnerability(Player player) {
        invulnerablePlayers.remove(player.getUniqueId());
    }

    private long getRemainingInvulnerableTime(Player player) {
        long currentTime = System.currentTimeMillis();
        long endTime = invulnerablePlayers.get(player.getUniqueId());
        return Math.max(0, endTime - currentTime);
    }

    // Method to remove invulnerability if an invulnerable player attacks a non-invulnerable player
    private void removeInvulnerabilityOnAttack(Player attacker, Player victim) {
        if (invulnerablePlayers.containsKey(attacker.getUniqueId()) && !invulnerablePlayers.containsKey(victim.getUniqueId())) {
            removeInvulnerability(attacker);
            attacker.sendMessage(ChatColor.RED + "무적 상태에서 다른 플레이어를 공격하여 무적이 해제되었습니다!");
        }
    }
}
