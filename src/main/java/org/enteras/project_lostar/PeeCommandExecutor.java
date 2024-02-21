package org.enteras.project_lostar;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PeeCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 이 명령어를 사용할 수 있습니다.");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("pee")) {
            // /give 명령어 실행
            player.performCommand("give @s honey_bottle{display:{Name:'[{\"text\":\"오줌\",\"italic\":false,\"bold\":true,\"color\":\"#ffff00\"}]',Lore:['[{\"text\":\"강석준이 싼 것 같다...\",\"italic\":false,\"color\":\"gray\"}]']}} 1");

            // /tellraw 명령어 실행
            player.performCommand("tellraw @s {\"text\":\"\\ucc0c\\ub9b0\\ub0b4\\uac00 \\ub098\\ub294 \\ub178\\ub780 \\ubb34\\uc5b8\\uac00\\uac00 \\ub4e0 \\ubcd1\\uc774 \\ub4e4\\uc5b4\\uc654\\ub2e4...\",\"bold\":true,\"color\":\"yellow\"}");

            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 0.7f, 0.1f);

            return true;
        }

        return false;
    }
}
