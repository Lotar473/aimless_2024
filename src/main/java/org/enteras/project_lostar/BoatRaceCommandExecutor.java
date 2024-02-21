package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;

public class BoatRaceCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "플레이어만 이 명령어를 사용할 수 있습니다.");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("boatrace")) {
            // tellraw 명령어를 플레이어에게 직접 실행
            player.sendMessage(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "[워프] " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "보트레이스 "+ ChatColor.WHITE + "(으)로 이동합니다...");
            player.sendActionBar(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "[워프] " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "보트레이스 "+ ChatColor.WHITE + "(으)로 이동합니다...");

            // 20틱(1초) 대기 후 작업 수행
            new BukkitRunnable() {
                @Override
                public void run() {
                    // 플레이어의 위치를 변경하여 텔레포트
                    player.teleport(new Location(player.getWorld(), -47, -60, 87));

                    // 플레이어에게 타이틀 메시지 표시
                    player.sendTitle(ChatColor.YELLOW + ChatColor.BOLD.toString() + "보트레이스", "", 10, 70, 20);

                    // 플레이어에게 사운드 재생
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.7f, 2f);
                }
            }.runTaskLater(DungPlugin.getPlugin(DungPlugin.class), 20);

            return true;
        }

        return false;
    }
}