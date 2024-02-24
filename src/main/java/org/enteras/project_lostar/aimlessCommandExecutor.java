package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;

public class aimlessCommandExecutor implements CommandExecutor, TabCompleter {

    private final Plugin plugin;
    private final aimlessHidePlayerList listener;

    // 첫 번째 생성자
    public aimlessCommandExecutor(Plugin plugin) {
        this.plugin = plugin;
        this.listener = new aimlessHidePlayerList(); // aimlessHidePlayerList 인스턴스 생성
    }

    // 두 번째 생성자
    public aimlessCommandExecutor(Plugin plugin, aimlessHidePlayerList listener) {
        this.plugin = plugin;
        this.listener = listener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("aimless")) {
            if (args.length >= 2 && args[0].equalsIgnoreCase("package")) {
                String password = args[1];

                if (password.equals("4732")) {
                    enableAllPackages(sender);
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "올바른 패스워드를 입력하세요.");
                    return true;
                }
            }

            sender.sendMessage("사용법: /aimless package <password>");
            return true;
        }
        return false;
    }

    private void enableAllPackages(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "모든 패키지가 활성화되었습니다.");
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        Bukkit.getPluginManager().registerEvents(new aimlessDeathMessage(), plugin);
        Bukkit.getPluginManager().registerEvents(new aimlessEmote(), plugin);
        Bukkit.getPluginManager().registerEvents(new aimlessCorpseChest(), plugin);
        Bukkit.getPluginManager().registerEvents(new aimlessPreventingMultiKills(), plugin);
        Bukkit.getPluginManager().registerEvents(new aimlessPlayerKillEffect(), plugin);
    }

    private void enableFeature(CommandSender sender, String subCommand) {
        switch (subCommand) {
            case "hideplayerlist":
                sender.sendMessage(ChatColor.GREEN + "aimlessHidePlayerList가 활성화되었습니다.");
                Bukkit.getPluginManager().registerEvents(listener, plugin);
                break;
            case "deathmessage":
                sender.sendMessage(ChatColor.GREEN + "aimlessDeathMessage가 활성화되었습니다.");
                // aimlessDeathMessage 리스너 등록
                Bukkit.getPluginManager().registerEvents(new aimlessDeathMessage(), DungPlugin.getPlugin(DungPlugin.class));
                break;
            case "emote":
                sender.sendMessage(ChatColor.GREEN + "Emote가 활성화되었습니다.");
                // aimlessEmote 리스너 등록
                Bukkit.getPluginManager().registerEvents(new aimlessEmote(), DungPlugin.getPlugin(DungPlugin.class));
                break;
            case "corpsechest":
                sender.sendMessage(ChatColor.GREEN + "aimlessCorpseChest가 활성화되었습니다.");
                Bukkit.getPluginManager().registerEvents(new aimlessCorpseChest(), DungPlugin.getPlugin(DungPlugin.class));
                break;
            case "preventmultikills":
                sender.sendMessage(ChatColor.GREEN + "aimlessPreventMultiKills가 활성화되었습니다.");
                Bukkit.getPluginManager().registerEvents(new aimlessPreventingMultiKills(), DungPlugin.getPlugin(DungPlugin.class));
                break;
            case "playerkilleffect":
                sender.sendMessage(ChatColor.GREEN + "aimlessPreventMultiKills가 활성화되었습니다.");
                Bukkit.getPluginManager().registerEvents(new aimlessPlayerKillEffect(), DungPlugin.getPlugin(DungPlugin.class));
                break;
            default:
                sender.sendMessage(ChatColor.RED + "올바르지 않은 서브 명령어입니다.");
                break;
        }
    }

    private void disableFeature(CommandSender sender, String subCommand) {
        switch (subCommand) {
            case "hideplayerlist":
                sender.sendMessage(ChatColor.GREEN + "aimlessPlayerList가 비활성화되었습니다.");
                break;
            case "deathmessage":
                sender.sendMessage(ChatColor.GREEN + "aimlessDeathMessage가 비활성화되었습니다.");
                break;
            case "emote":
                sender.sendMessage(ChatColor.GREEN + "Emote가 비활성화되었습니다.");
                break;
            case "corpsechest":
                sender.sendMessage(ChatColor.GREEN + "aimlessCorpseChest가 비활성화되었습니다.");
                break;
            case "preventmultikills":
                sender.sendMessage(ChatColor.GREEN + "aimlessPreventMultiKills가 비활성화되었습니다.");
                break;
            case "playerkilleffect":
                sender.sendMessage(ChatColor.GREEN + "aimlessPreventMultiKills가 비활성화되었습니다.");
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
            if ("corpsechest".startsWith(input)) {
                completions.add("corpsechest");
            }
            if ("preventmultikills".startsWith(input)) {
                completions.add("preventmultikills");
            }
            if ("playerkilleffect".startsWith(input)) {
                completions.add("playerkilleffect");
            }
            if ("package".startsWith(input)) {
                completions.add("package");
            }
        }
        return completions;
    }
}
