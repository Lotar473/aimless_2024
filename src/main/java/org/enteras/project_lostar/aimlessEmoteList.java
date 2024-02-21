package org.enteras.project_lostar;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class aimlessEmoteList implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("emotelist")) {
            sender.sendMessage(ChatColor.WHITE + "Available emotes:");
            sender.sendMessage(ChatColor.WHITE + "angry, heart, ㅗ, damage, critical, spit, no, note, rage, tear, lava, honey");
            return true;
        }
        return false;
    }
}
