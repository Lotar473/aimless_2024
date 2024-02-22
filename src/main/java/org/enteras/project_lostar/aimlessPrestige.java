package org.enteras.project_lostar;

import com.comphenix.protocol.utility.ChatExtensions;
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
            savePlayerKills(killerUUID, kills + 1); // Save the updated kills to configuration
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        int playerKills = getPlayerKills(player.getUniqueId());
        String playerRank = getRank(playerKills);

        // 채팅 메시지에 칭호 추가
        String chatMessage = String.format("[%s] %s: %s",
                playerRank, player.getName(), event.getMessage());
        event.setFormat(chatMessage);
    }

    // Load player kills from configuration
    private void loadPlayerKills() {
        for (String uuidString : config.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            int kills = config.getInt(uuidString);
            playerKills.put(uuid, kills);
        }
    }

    // Save player kills to configuration
    private void savePlayerKills(UUID uuid, int kills) {
        config.set(uuid.toString(), kills);
        plugin.saveConfig();
    }

    // Get player kills from the map
    private int getPlayerKills(UUID uuid) {
        return playerKills.getOrDefault(uuid, 0);
    }

    private String getRank(int kills) {
        if (kills >= 100) {
            return ChatColor.WHITE + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET + ChatColor.AQUA + ChatColor.BOLD.toString() + " ⚝L " + ChatColor.RESET + ChatColor.WHITE + ChatColor.MAGIC.toString() + "L" + ChatColor.RESET;
        } else if (kills >= 50) {
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
        } else if (kills == 39) {
            return ChatColor.GOLD + ChatColor.BOLD.toString() + "SS" + ChatColor.RESET;
        } else if (kills == 38) {
            return ChatColor.GOLD + ChatColor.BOLD.toString() + "SS" + ChatColor.RESET;
        } else if (kills == 37) {
            return ChatColor.GOLD + ChatColor.BOLD.toString() + "SS" + ChatColor.RESET;
        } else if (kills == 36) {
            return ChatColor.GOLD + ChatColor.BOLD.toString() + "SS" + ChatColor.RESET;
        } else if (kills == 35) {
            return ChatColor.GOLD + ChatColor.BOLD.toString() + "SS" + ChatColor.RESET;
        } else if (kills == 34) {
            return ChatColor.GOLD + ChatColor.BOLD.toString() + "SS" + ChatColor.RESET;
        } else if (kills == 33) {
            return ChatColor.GOLD + ChatColor.BOLD.toString() + "SS" + ChatColor.RESET;
        } else if (kills == 32) {
            return ChatColor.GOLD + ChatColor.BOLD.toString() + "SS" + ChatColor.RESET;
        } else if (kills == 31) {
            return ChatColor.GOLD + ChatColor.BOLD.toString() + "SS" + ChatColor.RESET;
        } else if (kills == 30) {
            return ChatColor.GOLD + ChatColor.BOLD.toString() + "SS" + ChatColor.RESET;
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
}
