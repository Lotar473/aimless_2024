package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class InventoryChangePlugin {

    private DungPlugin plugin;

    private boolean inventoryChangeEnabled = false;

    public InventoryChangePlugin(DungPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isInventoryChangeEnabled() {
        return inventoryChangeEnabled;
    }

    public void setInventoryChangeEnabled(boolean enabled) {
        this.inventoryChangeEnabled = enabled;
    }

    public void changeInventories() {
        if (!inventoryChangeEnabled) {
            return;
        }

        Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);

        final String TitleMessage3 = "";

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.broadcastMessage(ChatColor.WHITE + "유체이탈 3초 전...");
            for (Player onlinePlayer : onlinePlayers) {
                sendTitle(onlinePlayer, TitleMessage3, "3", 0, 20, 0);
            }
        }, 20);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player onlinePlayer : onlinePlayers) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), "minecraft:block.note_block.hat", 1f, 1f);
            }
        }, 20);

        final String TitleMessage2 = "";
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.broadcastMessage(ChatColor.WHITE + "유체이탈 2초 전...");
            for (Player onlinePlayer : onlinePlayers) {
                sendTitle(onlinePlayer, TitleMessage2, "2", 0, 20, 0);
            }
        }, 40);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player onlinePlayer : onlinePlayers) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), "minecraft:block.note_block.hat", 1f, 1f);
            }
        }, 40);

        final String TitleMessage1 = "";
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (onlinePlayers.length < 2) {
                Bukkit.broadcastMessage(ChatColor.RED + "대상 플레이어가 충분하지 않습니다.");
            } else {
                Bukkit.broadcastMessage(ChatColor.WHITE + "유체이탈 1초 전...");
                for (Player onlinePlayer : onlinePlayers) {
                    sendTitle(onlinePlayer, TitleMessage1, "1", 0, 20, 0);
                }
            }
        }, 60);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player onlinePlayer : onlinePlayers) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), "minecraft:block.note_block.hat", 1f, 1f);
            }
        }, 60);

        if (onlinePlayers.length < 2) {
            return;
        }

        Random random = new Random();
        List<Player> playersToSwap = new ArrayList<>();

        for (Player player : onlinePlayers) {
            playersToSwap.add(player);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (int i = 0; i < playersToSwap.size() - 1; i++) {
                Player player1 = playersToSwap.get(i);
                Player player2 = playersToSwap.get(i + 1 >= playersToSwap.size() ? i : i + 1);

                swapInventories(player1, player2);
                swapHealthAndFood(player1, player2);
                swapExperienceLevels(player1, player2);

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), "minecraft:block.ender_chest.open", 1.0F, 1.0F);
                }
            }
        }, 80);
    }

    private void swapInventories(Player player1, Player player2) {
        ItemStack[] player1Contents = player1.getInventory().getContents().clone();
        ItemStack[] player2Contents = player2.getInventory().getContents().clone();

        player1.getInventory().setContents(player2Contents);
        player2.getInventory().setContents(player1Contents);

        player1.updateInventory();
        player2.updateInventory();
    }

    private void swapHealthAndFood(Player player1, Player player2) {
        double health1 = player1.getHealth();
        double health2 = player2.getHealth();

        int foodLevel1 = player1.getFoodLevel();
        int foodLevel2 = player2.getFoodLevel();

        player1.setHealth(health2);
        player2.setHealth(health1);

        player1.setFoodLevel(foodLevel2);
        player2.setFoodLevel(foodLevel1);
    }

    private void swapExperienceLevels(Player player1, Player player2) {
        int level1 = player1.getLevel();
        int level2 = player2.getLevel();

        player1.setLevel(level2);
        player2.setLevel(level1);
    }

    private void sendTitle(Player player, String mainTitle, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(mainTitle, subtitle, fadeIn, stay, fadeOut);
    }
}