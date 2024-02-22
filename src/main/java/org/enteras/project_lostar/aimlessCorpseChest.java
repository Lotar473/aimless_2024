package org.enteras.project_lostar;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
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

        // 왼손에 있는 아이템을 드랍
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        if (offHandItem != null && offHandItem.getType() != Material.AIR) {
            player.getWorld().dropItemNaturally(deathLocation, offHandItem);
        }

        // 상자 생성
        Chest chest = createCorpseChest(deathLocation);
        if (chest == null) {
            return;
        }

        Inventory chestInventory = chest.getBlockInventory();

        // 플레이어의 아이템을 상자에 추가
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR && !isInOffHand(player, item)) {
                if (chestInventory.firstEmpty() != -1) {
                    chestInventory.addItem(item);
                } else {
                    player.getWorld().dropItemNaturally(deathLocation, item); // 상자에 넣을 수 없는 아이템은 플레이어 위치에 드랍
                }
            }
        }

        // 표지판 생성
        createSignAboveChest(chest, player.getName());

        // 핫바 슬롯의 아이템을 드롭
        for (ItemStack item : player.getInventory().getExtraContents()) {
            if (item != null && item.getType() != Material.AIR && !isInOffHand(player, item)) {
                player.getWorld().dropItemNaturally(deathLocation, item); // 왼손 슬롯 아이템도 드롭
            }
        }

        // 원래의 드롭 아이템 제거
        event.getDrops().clear();
    }

    // 왼손 슬롯에 아이템이 있는지 확인하는 유틸리티 메서드
    private boolean isInOffHand(Player player, ItemStack item) {
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        return offHandItem != null && offHandItem.equals(item);
    }

    private Chest createCorpseChest(Location location) {
        Block currentBlock = location.getBlock();

        // 블록을 위로 탐색하여 공기가 아닌 첫 번째 블록 탐색
        while (currentBlock.getType() == Material.AIR && currentBlock.getY() > 0) {
            currentBlock = currentBlock.getRelative(BlockFace.DOWN);
        }

        // 상자 생성
        currentBlock = currentBlock.getRelative(BlockFace.DOWN); // 블록을 1칸 아래로 이동
        currentBlock.setType(Material.CHEST);

        BlockState blockState = currentBlock.getState();
        if (blockState instanceof Chest) {
            return (Chest) blockState;
        }

        return null;
    }

    private void createSignAboveChest(Chest chest, String playerName) {
        Location signLocation = chest.getLocation().add(0, 1, 0);
        Block signBlock = signLocation.getBlock();
        signBlock.setType(Material.OAK_SIGN);

        BlockState state = signBlock.getState();
        if (state instanceof Sign) {
            Sign sign = (Sign) state;
            sign.setLine(1, playerName);
            sign.setLine(2, "여기에 잠들다.");
            sign.setGlowingText(true); // 발광 먹물 적용
            sign.update();
        }
    }
}
