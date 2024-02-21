package org.enteras.project_lostar;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Sound;

public class MondstadtCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "플레이어만 이 명령어를 사용할 수 있습니다.");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("mondstadt")) {
            player.sendMessage(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "[워프] " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "몬드성 "+ ChatColor.WHITE + "(으)로 이동합니다...");
            player.sendActionBar(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "[워프] " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "몬드성 "+ ChatColor.WHITE + "(으)로 이동합니다...");

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(new Location(player.getWorld(), -783, -52, 305));
                    player.sendTitle(ChatColor.WHITE + "몬드성", "", 10, 70, 20);
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.7f, 2f);
                }
            }.runTaskLater(DungPlugin.getPlugin(DungPlugin.class), 20);

            return true;
        }

        return false;
    }

    private void runConsoleCommand(String command) {
        DungPlugin.getPlugin(DungPlugin.class).getServer().dispatchCommand(DungPlugin.getPlugin(DungPlugin.class).getServer().getConsoleSender(), command);
    }
}
