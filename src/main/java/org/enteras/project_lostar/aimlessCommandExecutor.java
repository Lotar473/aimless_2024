package org.enteras.project_lostar;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class aimlessCommandExecutor implements CommandExecutor, TabCompleter {
    private boolean isHidePlayerListEnabled = false;
    private boolean isDeathMessageEnabled = false;
    private boolean isEmoteEnabled = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("aimless")) {
            if (args.length >= 2) {
                String subCommand = args[0].toLowerCase();
                String password = args[1];

                if (password.equals("4732")) {
                    enableFeature(sender, subCommand);
                    return true;
                } else if (password.equals("0473")) {
                    disableFeature(sender, subCommand);
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "올바른 패스워드를 입력하세요.");
                    return true;
                }
            }

            sender.sendMessage("사용법: /aimless <subCommand> <password>");
            return true;
        }
        return false;
    }

    private void enableFeature(CommandSender sender, String subCommand) {
        switch (subCommand) {
            case "hideplayerlist":
                isHidePlayerListEnabled = true;
                sender.sendMessage(ChatColor.GREEN + "aimlessPlayerList가 활성화되었습니다.");
                if (isHidePlayerListEnabled) {
                    // aimlessHidePlayerList 리스너를 등록합니다.
                    Bukkit.getPluginManager().registerEvents(new aimlessHidePlayerList(), DungPlugin.getPlugin(DungPlugin.class));
                }
                break;
            case "deathmessage":
                isDeathMessageEnabled = true;
                sender.sendMessage(ChatColor.GREEN + "aimlessDeathMessage가 활성화되었습니다.");
                if (isDeathMessageEnabled) {
                    // aimlessDeathMessage 리스너를 등록합니다.
                    Bukkit.getPluginManager().registerEvents(new aimlessDeathMessage(), DungPlugin.getPlugin(DungPlugin.class));
                }
                break;
            case "emote":
                isEmoteEnabled = true;
                sender.sendMessage(ChatColor.GREEN + "Emote가 활성화되었습니다.");
                if (isEmoteEnabled) {
                    // aimlessEmote 리스너를 등록합니다.
                    Bukkit.getPluginManager().registerEvents(new aimlessEmote(), DungPlugin.getPlugin(DungPlugin.class));
                }
                break;
            default:
                sender.sendMessage(ChatColor.RED + "올바르지 않은 서브 명령어입니다.");
                break;
        }
    }

    private void disableFeature(CommandSender sender, String subCommand) {
        switch (subCommand) {
            case "hideplayerlist":
                isHidePlayerListEnabled = false;
                sender.sendMessage(ChatColor.GREEN + "aimlessPlayerList가 비활성화되었습니다.");
                break;
            case "deathmessage":
                isDeathMessageEnabled = false;
                sender.sendMessage(ChatColor.GREEN + "aimlessDeathMessage가 비활성화되었습니다.");
                break;
            case "emote":
                isEmoteEnabled = false;
                sender.sendMessage(ChatColor.GREEN + "Emote가 비활성화되었습니다.");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "올바르지 않은 서브 명령어입니다.");
                break;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            if ("hideplayerlist".startsWith(input)) {
                completions.add("hideplayerlist");
            }
            if ("deathmessage".startsWith(input)) {
                completions.add("deathmessage");
            }
            if ("emote".startsWith(input)) {
                completions.add("emote");
            }
        }
        return completions;
    }
}
