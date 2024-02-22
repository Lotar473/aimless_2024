package org.enteras.project_lostar;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class aimlessCorpseChest implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();

        // 상자 생성
        Chest chest = createCorpseChest(deathLocation);
        if (chest == null) {
            return;
        }

        Inventory chestInventory = chest.getBlockInventory();

        // 플레이어의 아이템을 상자에 추가
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                if (chestInventory.firstEmpty() != -1) {
                    chestInventory.addItem(item);
                } else {
                    player.getWorld().dropItemNaturally(deathLocation, item);
                }
            }
        }

        // 핫바 슬롯의 아이템을 드롭
        for (ItemStack item : player.getInventory().getExtraContents()) {
            if (item != null && item.getType() != Material.AIR) {
                player.getWorld().dropItemNaturally(deathLocation, item);
            }
        }

        // 원래의 드롭 아이템 제거
        event.getDrops().clear();
    }

    private Chest createCorpseChest(Location location) {
        Block chestBlock = location.getBlock().getRelative(BlockFace.UP);
        chestBlock.setType(Material.CHEST);
        BlockState blockState = chestBlock.getState();
        if (blockState instanceof Chest) {
            return (Chest) blockState;
        }
        return null;
    }
}
