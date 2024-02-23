package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class aimlessPrestige implements Listener {
    private final Map<UUID, Integer> playerKills = new HashMap<>();
    private final JavaPlugin plugin;
    private final FileConfiguration config;

    public aimlessPrestige(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        loadPlayerKills();
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
            savePlayerKills(killerUUID, kills + 1); // 업데이트된 킬을 설정에 저장
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        int kills = getPlayerKills(player.getUniqueId());
        String rank = getRank(kills);

        // 채팅 메시지에 랭크와 킬 수 추가
        String chatMessage = String.format("[%s] %s %s: %s",
                rank, formatKills(kills), player.getName(), event.getMessage());
        event.setFormat(chatMessage);
    }

    // 설정에서 킬을 불러옴
    private void loadPlayerKills() {
        for (String uuidString : config.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            int kills = config.getInt(uuidString);
            playerKills.put(uuid, kills);
        }
    }

    // 설정에 킬을 저장
    private void savePlayerKills(UUID uuid, int kills) {
        config.set(uuid.toString(), kills);
        plugin.saveConfig();
    }

    // 맵에서 플레이어의 킬을 가져옴
    private int getPlayerKills(UUID uuid) {
        return playerKills.getOrDefault(uuid, 0);
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

        } else {
            color = ChatColor.DARK_GRAY;
            symbol = "[0✫]";
        }
        return "" + color + symbol + ChatColor.RESET + "";
    }
}
