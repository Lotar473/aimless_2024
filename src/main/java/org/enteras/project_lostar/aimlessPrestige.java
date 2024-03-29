package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import net.md_5.bungee.api.ChatColor;

public class aimlessPrestige implements Listener, CommandExecutor {
    private final Map<UUID, Integer> playerKills = new HashMap<>();
    private final Map<UUID, String> playerTitles = new HashMap<>();
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final ConfigurationSection killsSection;

    public aimlessPrestige(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        // "kills" 섹션의 하위 섹션을 가져옴
        killsSection = config.getConfigurationSection("kills");
        if (killsSection != null) {
            for (String uuidString : killsSection.getKeys(false)) {
                UUID uuid = UUID.fromString(uuidString);
                int kills = killsSection.getInt(uuidString);
                playerKills.put(uuid, kills);

                // 칭호 정보도 로드할 수 있음
                String title = config.getString("titles." + uuidString);
                if (title != null && !title.isEmpty()) {
                    playerTitles.put(uuid, title);
                }
            }
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if (killer != null) {
            UUID killerUUID = killer.getUniqueId();
            int kills = getPlayerKills(killerUUID);
            playerKills.put(killerUUID, kills + 1);
            savePlayerKills(killerUUID);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        int kills = getPlayerKills(player.getUniqueId());
        String rank = getRank(kills);
        String title = getPlayerTitle(player.getUniqueId());

        // 칭호가 있는 경우에만 추가합니다.
        String titleMessage = "";
        if (title != null && !title.isEmpty()) {
            titleMessage = ChatColor.translateAlternateColorCodes('&', title) + " ";
        }

        String chatMessage = String.format("[%s] %s %s%s: %s",
                rank, formatKills(kills), titleMessage, player.getName(), event.getMessage());

        event.setFormat(chatMessage);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("aimlesstitle")) {
            if (args.length >= 2) {
                int titleIndex;
                try {
                    titleIndex = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.of("#cab5ff") + "칭호 인덱스는 숫자여야 합니다.");
                    return false;
                }

                String playerName = args[1];
                Player targetPlayer = Bukkit.getPlayer(playerName);

                if (targetPlayer == null || !targetPlayer.isOnline()) {
                    sender.sendMessage(ChatColor.RED + "플레이어 " + playerName + "을(를) 찾을 수 없습니다.");
                    return false;
                }

                loadPlayerData();

                String title = getTitleByIndex(titleIndex);
                if (title == null) {
                    sender.sendMessage(ChatColor.RED + "해당 인덱스에 해당하는 칭호를 찾을 수 없습니다.");
                    return false;
                }

                setPlayerTitle(targetPlayer.getUniqueId(), title);
                sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + "님에게 칭호가 설정되었습니다: " + title);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "사용법: /aimlesstitle <칭호 인덱스> <플레이어 닉네임>");
                return false;
            }
        }
        return false;
    }


    private void loadPlayerData() {
        // "titles" 섹션의 모든 플레이어 칭호를 가져옴
        ConfigurationSection titlesSection = config.getConfigurationSection("titles");
        if (titlesSection != null) {
            for (String uuidString : titlesSection.getKeys(false)) {
                UUID uuid = UUID.fromString(uuidString);

                // 플레이어의 칭호 정보를 가져와서 맵에 추가
                String title = titlesSection.getString(uuidString);
                if (title != null && !title.isEmpty()) {
                    playerTitles.put(uuid, title);
                }
            }
        }
    }


    private void savePlayerKills(UUID uuid) {
        int kills = playerKills.get(uuid);
        config.set("kills." + uuid.toString(), kills);
        plugin.saveConfig();
    }

    private int getPlayerKills(UUID uuid) {
        return playerKills.getOrDefault(uuid, 0);
    }

    private void setPlayerTitle(UUID uuid, String title) {
        playerTitles.put(uuid, title);
        config.set("titles." + uuid.toString(), title); // 플레이어의 칭호를 config.yml에 저장
        plugin.saveConfig(); // 변경 사항 저장
    }

    private String getPlayerTitle(UUID uuid) {
        return playerTitles.getOrDefault(uuid, null);
    }

    private String getTitleByIndex(int index) {
        switch (index) {
            case 1:
                return ChatColor.DARK_RED + "[" + ChatColor.RED + ChatColor.BOLD.toString() + "First Blood" + ChatColor.RESET + ChatColor.DARK_RED + "] [" +
                        ChatColor.of("#FF0000") + "☠" + ChatColor.RESET + "]";
            case 2:
                return "[" + ChatColor.of("#FF0000") + "관리자를 잡은 자" + ChatColor.RESET + "]";
            case 3:
                return ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "뉴비" + ChatColor.DARK_GREEN + "]";
            case 4:
                return "[" + ChatColor.GREEN + ChatColor.BOLD + "Season 1 Player" + ChatColor.RESET + "]";
            case 5:
                return "[" + ChatColor.of("#5c00ff") + ChatColor.BOLD.toString() + "D" + ChatColor.of("#6306ff") + ChatColor.BOLD.toString() + "R" + ChatColor.of("#6b0cff") +
                        ChatColor.BOLD.toString() + "A" + ChatColor.of("#7212ff") + ChatColor.BOLD.toString() + "G" + ChatColor.of("#7a18ff") + ChatColor.BOLD.toString() + "O" + ChatColor.of("#821eff") +
                        ChatColor.BOLD.toString() + "N " + ChatColor.of("#8924ff") + ChatColor.BOLD.toString() + "S" + ChatColor.of("#912aff") + ChatColor.BOLD.toString() + "L" + ChatColor.of("#9930ff") +
                        ChatColor.BOLD.toString() + "A" + ChatColor.of("#a036ff") + ChatColor.BOLD.toString() + "Y" + ChatColor.of("#a83cff") + ChatColor.BOLD.toString() + "E" + ChatColor.of("#b042ff") + ChatColor.BOLD.toString() + "R" + ChatColor.RESET + "] [" + ChatColor.DARK_PURPLE + "☬" + ChatColor.RESET + "]";
            case 6:
                return "";
            case 7:
                return "";
            case 8:
                return "";
            case 9:
                return "";
            case 10:
                return "";
            default:
                return null;
        }
    }

    private String getRank(int kills) {
        if (kills >= 100 && kills < 200) {
            return ChatColor.WHITE + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.AQUA + ChatColor.BOLD.toString() + " ⚝L " + ChatColor.RESET + ChatColor.WHITE + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills >= 50 && kills < 100) {
            return ChatColor.WHITE + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + " X " + ChatColor.RESET + ChatColor.WHITE + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills == 49) {
            return ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD.toString() + " U " + ChatColor.RESET + ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills == 48) {
            return ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD.toString() + " U " + ChatColor.RESET + ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills == 47) {
            return ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD.toString() + " U " + ChatColor.RESET + ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills == 46) {
            return ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD.toString() + " U " + ChatColor.RESET + ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills == 45) {
            return ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD.toString() + " U " + ChatColor.RESET + ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills == 44) {
            return ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD.toString() + " U " + ChatColor.RESET + ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills == 43) {
            return ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD.toString() + " U " + ChatColor.RESET + ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills == 42) {
            return ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD.toString() + " U " + ChatColor.RESET + ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills == 41) {
            return ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD.toString() + " U " + ChatColor.RESET + ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills == 40) {
            return ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD.toString() + " U " + ChatColor.RESET + ChatColor.GOLD + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;

        } else if (kills == 39 || kills == 38 || kills == 37 || kills == 36 || kills == 35 || kills == 34 || kills == 33 || kills == 32 || kills == 31 || kills == 30) {
            return ChatColor.WHITE + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.GOLD + ChatColor.BOLD.toString() + " SS " + ChatColor.RESET + ChatColor.WHITE + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;

        } else if (kills == 29) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 28) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 27) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 26) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 25) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 24) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 23) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 22) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 21) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 20) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 19) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 18) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 17) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;
        } else if (kills == 16) {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "S+" + ChatColor.RESET;

        } else if (kills == 15) {
            return ChatColor.YELLOW + "S" + ChatColor.RESET;
        } else if (kills == 14) {
            return ChatColor.YELLOW + "S" + ChatColor.RESET;
        } else if (kills == 13) {
            return ChatColor.YELLOW + "S" + ChatColor.RESET;
        } else if (kills == 12) {
            return ChatColor.YELLOW + "S-" + ChatColor.RESET;
        } else if (kills == 11) {
            return ChatColor.YELLOW + "S-" + ChatColor.RESET;

        } else if (kills == 10) {
            return ChatColor.GREEN + "A+" + ChatColor.RESET;
        } else if (kills == 9) {
            return ChatColor.DARK_GREEN + "A" + ChatColor.RESET;
        } else if (kills == 8) {
            return ChatColor.GREEN + "A-" + ChatColor.RESET;

        } else if (kills == 7) {
            return ChatColor.DARK_AQUA + "B+" + ChatColor.RESET;
        } else if (kills == 6) {
            return ChatColor.DARK_AQUA + "B" + ChatColor.RESET;
        } else if (kills == 5) {
            return ChatColor.BLUE + "B-" + ChatColor.RESET;

        } else if (kills == 4) {
            return ChatColor.DARK_PURPLE + "C+" + ChatColor.RESET;
        } else if (kills == 3) {
            return ChatColor.DARK_PURPLE + "C" + ChatColor.RESET;
        } else if (kills == 2) {
            return ChatColor.DARK_PURPLE + "C-" + ChatColor.RESET;

        } else {
            return ChatColor.GRAY + "D" + ChatColor.RESET;
        }

    }
    private String formatKills(int kills) {
        ChatColor color;
        String symbol;
        if (kills == 1) {
            color = ChatColor.DARK_GRAY;
            symbol = "[1✫]";
        } else if (kills == 2) {
            color = ChatColor.GRAY;
            symbol = "[2✫]";
        } else if (kills == 3) {
            color = ChatColor.GRAY;
            symbol = "[3✫]";
        } else if (kills == 4) {
            color = ChatColor.GOLD;
            symbol = "[4✫]";
        } else if (kills == 5) {
            color = ChatColor.GOLD;
            symbol = "[5✫]";
        } else if (kills == 6) {
            color = ChatColor.GOLD;
            symbol = "[6✫]";
        } else if (kills == 7) {
            color = ChatColor.AQUA;
            symbol = "[7✫]";
        } else if (kills == 8) {
            color = ChatColor.AQUA;
            symbol = "[" + "8✫" + "]";
        } else if (kills == 9) {
            color = ChatColor.AQUA;
            symbol = "[" + "9✫" + "]";
        } else if (kills == 10) {
            color = ChatColor.AQUA;
            symbol = "[" + "10✫" + "]";
        } else if (kills == 11) {
            color = ChatColor.DARK_GREEN;
            symbol = "[" + "11✫" + "]";
        } else if (kills == 12) {
            color = ChatColor.DARK_GREEN;
            symbol = "[" + "12✫" + "]";
        } else if (kills == 13) {
            color = ChatColor.DARK_GREEN;
            symbol = "[" + "13✫" + "]";
        } else if (kills == 14) {
            color = ChatColor.DARK_GREEN;
            symbol = "[" + "14✫" + "]";
        } else if (kills == 15) {
            color = ChatColor.DARK_GREEN;
            symbol = "[" + "15✫" + "]";
        } else if (kills == 16) {
            color = ChatColor.DARK_AQUA;
            symbol = "[" + "16✫" + "]";
        } else if (kills == 17) {
            color = ChatColor.DARK_AQUA;
            symbol = "[" + "17✫" + "]";
        } else if (kills == 18) {
            color = ChatColor.DARK_AQUA;
            symbol = "[" + "18✫" + "]";
        } else if (kills == 19) {
            color = ChatColor.DARK_AQUA;
            symbol = "[" + "19✫" + "]";
        } else if (kills == 20) {
            color = ChatColor.DARK_AQUA;
            symbol = "[" + "20✫" + "]";
        } else if (kills == 21) {
            color = ChatColor.RED;
            symbol = "[" + "21✫" + "]";
        } else if (kills == 22) {
            color = ChatColor.RED;
            symbol = "[" + "22✫" + "]";
        } else if (kills == 23) {
            color = ChatColor.RED;
            symbol = "[" + "23✫" + "]";
        } else if (kills == 24) {
            color = ChatColor.RED;
            symbol = "[" + "24✫" + "]";
        } else if (kills == 25) {
            color = ChatColor.RED;
            symbol = "[" + "25✫" + "]";
        } else if (kills >= 26 && kills < 30) {
            color = ChatColor.LIGHT_PURPLE;
            symbol = "[" + kills + "✫" + "]";
        } else if (kills >= 30 && kills < 40) {
            color = ChatColor.BLUE;
            symbol = "[" + kills + "✫" + "]";
        } else if (kills >= 40 && kills < 50) {
            color = ChatColor.DARK_BLUE;
            symbol = "[" + kills + "✫" + "]";
        } else if (kills >= 50 && kills < 60) {
            color = ChatColor.AQUA;
            symbol = ChatColor.BOLD + "[" + ChatColor.BOLD.toString() + kills + "✫" + "]";
        } else if (kills >= 60 && kills < 70) {
            color = ChatColor.GREEN;
            symbol = ChatColor.BOLD + "[" + ChatColor.BOLD.toString() + kills + "✫" + "]";
        } else if (kills >= 70 && kills < 80) {
            color = ChatColor.YELLOW;
            symbol = ChatColor.BOLD + "[" + ChatColor.BOLD.toString() + kills + "✫" + "]";
        } else if (kills >= 80 && kills < 90) {
            color = ChatColor.RED;
            symbol = ChatColor.BOLD + "[" + ChatColor.BOLD.toString() + kills + "✫" + "]";
        } else if (kills >= 90 && kills < 100) {
            color = ChatColor.DARK_RED;
            symbol = ChatColor.BOLD + "[" + ChatColor.BOLD.toString() + kills + "✫" + "]";
        } else if (kills == 100) {
            color = ChatColor.RED;
            symbol = "[" + ChatColor.GOLD + ChatColor.BOLD.toString() + "1" + ChatColor.YELLOW + ChatColor.BOLD.toString() + "0" + ChatColor.GREEN + ChatColor.BOLD.toString() + "0" + ChatColor.AQUA + ChatColor.BOLD.toString() + "✫" + ChatColor.LIGHT_PURPLE + "]";
        } else if (kills == 101) { //101킬
            color = ChatColor.RED;
            symbol = "[" + ChatColor.GOLD + ChatColor.BOLD.toString() + "1" + ChatColor.YELLOW + ChatColor.BOLD.toString() + "0" + ChatColor.GREEN + ChatColor.BOLD.toString() + "1" + ChatColor.AQUA + ChatColor.BOLD.toString() + "✫" + ChatColor.LIGHT_PURPLE + "]";
        } else if (kills == 102) { //102킬
            color = ChatColor.RED;
            symbol = "[" + ChatColor.GOLD + ChatColor.BOLD.toString() + "1" + ChatColor.YELLOW + ChatColor.BOLD.toString() + "0" + ChatColor.GREEN + ChatColor.BOLD.toString() + "2" + ChatColor.AQUA + ChatColor.BOLD.toString() + "✫" + ChatColor.LIGHT_PURPLE + "]";
        } else if (kills == 103) { //103킬
            color = ChatColor.RED;
            symbol = "[" + ChatColor.GOLD + ChatColor.BOLD.toString() + "1" + ChatColor.YELLOW + ChatColor.BOLD.toString() + "0" + ChatColor.GREEN + ChatColor.BOLD.toString() + "3" + ChatColor.AQUA + ChatColor.BOLD.toString() + "✫" + ChatColor.LIGHT_PURPLE + "]";
        } else if (kills == 104) { //104킬
            color = ChatColor.RED;
            symbol = "[" + ChatColor.GOLD + ChatColor.BOLD.toString() + "1" + ChatColor.YELLOW + ChatColor.BOLD.toString() + "0" + ChatColor.GREEN + ChatColor.BOLD.toString() + "4" + ChatColor.AQUA + ChatColor.BOLD.toString() + "✫" + ChatColor.LIGHT_PURPLE + "]";
        } else if (kills == 105) { //105킬
            color = ChatColor.RED;
            symbol = "[" + ChatColor.GOLD + ChatColor.BOLD.toString() + "1" + ChatColor.YELLOW + ChatColor.BOLD.toString() + "0" + ChatColor.GREEN + ChatColor.BOLD.toString() + "5" + ChatColor.AQUA + ChatColor.BOLD.toString() + "✫" + ChatColor.LIGHT_PURPLE + "]";
        } else if (kills == 106) { //106킬
            color = ChatColor.RED;
            symbol = "[" + ChatColor.GOLD + ChatColor.BOLD.toString() + "1" + ChatColor.YELLOW + ChatColor.BOLD.toString() + "0" + ChatColor.GREEN + ChatColor.BOLD.toString() + "6" + ChatColor.AQUA + ChatColor.BOLD.toString() + "✫" + ChatColor.LIGHT_PURPLE + "]";
        } else if (kills == 107) { //107킬
            color = ChatColor.RED;
            symbol = "[" + ChatColor.GOLD + ChatColor.BOLD.toString() + "1" + ChatColor.YELLOW + ChatColor.BOLD.toString() + "0" + ChatColor.GREEN + ChatColor.BOLD.toString() + "7" + ChatColor.AQUA + ChatColor.BOLD.toString() + "✫" + ChatColor.LIGHT_PURPLE + "]";
        } else if (kills == 108) { //108킬
            color = ChatColor.RED;
            symbol = "[" + ChatColor.GOLD + ChatColor.BOLD.toString() + "1" + ChatColor.YELLOW + ChatColor.BOLD.toString() + "0" + ChatColor.GREEN + ChatColor.BOLD.toString() + "8" + ChatColor.AQUA + ChatColor.BOLD.toString() + "✫" + ChatColor.LIGHT_PURPLE + "]";
        } else if (kills == 109) { //109킬
            color = ChatColor.RED;
            symbol = "[" + ChatColor.GOLD + ChatColor.BOLD.toString() + "1" + ChatColor.YELLOW + ChatColor.BOLD.toString() + "0" + ChatColor.GREEN + ChatColor.BOLD.toString() + "9" + ChatColor.AQUA + ChatColor.BOLD.toString() + "✫" + ChatColor.LIGHT_PURPLE + "]";

        } else {
            color = ChatColor.DARK_GRAY;
            symbol = "[0✫]";
        }
        return "" + color + symbol + ChatColor.RESET + "";
    }
}
