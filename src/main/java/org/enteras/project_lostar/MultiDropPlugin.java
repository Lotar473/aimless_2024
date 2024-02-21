package org.enteras.project_lostar;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class MultiDropPlugin implements CommandExecutor, Listener {
    private boolean isMultiDropsEnabled = true;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("multidrops")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("4732")) {
                    enableMultiDrops(sender);
                    return true;
                } else if (args[0].equalsIgnoreCase("0473")) {
                    disableMultiDrops(sender);
                    return true;
                }
            }

            sender.sendMessage("사용법: /multidrops <password>");
            return true;
        }
        return false;
    }

    private void enableMultiDrops(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("multidrops.toggle")) {
                if (!isMultiDropsEnabled) {
                    isMultiDropsEnabled = true;
                    player.sendMessage(ChatColor.GREEN + "멀티드롭이 활성화되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
                } else {
                    player.sendMessage(ChatColor.WHITE + "멀티드롭은 이미 활성화되어 있습니다.");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.5f);
                }
            } else {
                player.sendMessage(ChatColor.RED + "권한이 없습니다.");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.5f);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "플레이어만 이 명령어를 사용할 수 있습니다.");
        }
    }

    private void disableMultiDrops(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("multidrops.toggle")) {
                if (isMultiDropsEnabled) {
                    isMultiDropsEnabled = false;
                    player.sendMessage(ChatColor.GREEN + "멀티드롭이 비활성화되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
                } else {
                    player.sendMessage(ChatColor.WHITE + "멀티드롭은 이미 비활성화되어 있습니다.");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.5f);
                }
            } else {
                player.sendMessage(ChatColor.RED + "권한이 없습니다.");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.5f);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "플레이어만 이 명령어를 사용할 수 있습니다.");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isMultiDropsEnabled) {
            event.setDropItems(false);
            for (ItemStack drop : event.getBlock().getDrops()) {
                drop.setAmount(drop.getAmount() * 5);
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (isMultiDropsEnabled) {
            Entity entity = event.getEntity();
            for (ItemStack drop : event.getDrops()) {
                drop.setAmount(drop.getAmount() * 5);
            }
        }
    }
}