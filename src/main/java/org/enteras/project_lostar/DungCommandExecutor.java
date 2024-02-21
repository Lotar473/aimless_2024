package org.enteras.project_lostar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Sound;

public class DungCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 이 명령어를 사용할 수 있습니다.");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("dung")) {
            // /give 명령어 실행
            player.performCommand("give @s brown_dye{display:{Name:'[{\"text\":\"똥\",\"italic\":false,\"color\":\"#663300\",\"bold\":true}]'}} 65");

            // /tellraw 명령어 실행
            player.performCommand("tellraw @s {\"text\":\"\\uac08\\uc0c9\\uc758 \\ubb34\\uc5b8\\uac00\\uac00 \\ub4e4\\uc5b4\\uc654\\ub2e4...\",\"color\":\"#663300\",\"bold\":true}");

            player.playSound(player.getLocation(), Sound.BLOCK_HONEY_BLOCK_BREAK, 0.7f, 0.5f);

            return true;
        }

        return false;
    }
}
