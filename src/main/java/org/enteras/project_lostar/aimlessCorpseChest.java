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

        // 블록이 이미 채워져 있는지 확인
        if (currentBlock.getType() != Material.AIR) {
            // 블록이 이미 채워져 있으면 위로 쭉 올라가면서 빈 공간을 찾음
            while (currentBlock.getType() != Material.AIR && currentBlock.getY() < 255) {
                currentBlock = currentBlock.getRelative(BlockFace.UP);
            }

            // 빈 공간을 찾은 경우 상자 설치
            if (currentBlock.getType() == Material.AIR) {
                currentBlock.setType(Material.CHEST);
                BlockState blockState = currentBlock.getState();
                if (blockState instanceof Chest) {
                    return (Chest) blockState;
                }
            }
        } else {
            // 블록이 비어있는 경우 상자 생성
            currentBlock.setType(Material.CHEST);
            BlockState blockState = currentBlock.getState();
            if (blockState instanceof Chest) {
                return (Chest) blockState;
            }
        }

        return null;
    }

    private void createSignAboveChest(Chest chest, String playerName) {
        Location signLocation = chest.getLocation().clone().add(0, 1, 0);
        Block signBlock = signLocation.getBlock();

        // 상자 바로 위에 표지판 설치
        if (signBlock.getType() != Material.AIR) {
            // 블록이 이미 채워져 있는 경우 위로 쭉 올라가면서 빈 공간을 찾음
            while (signBlock.getType() != Material.AIR && signBlock.getY() < 255) {
                signBlock = signBlock.getRelative(BlockFace.UP);
            }

            // 빈 공간을 찾은 경우 표지판 설치
            if (signBlock.getType() == Material.AIR) {
                signBlock.setType(Material.OAK_SIGN);
                BlockState state = signBlock.getState();
                if (state instanceof Sign) {
                    Sign sign = (Sign) state;
                    sign.setLine(1, playerName);
                    sign.setLine(2, "여기에 잠들다.");
                    sign.setGlowingText(true); // 발광 텍스트 적용
                    sign.update();
                }
            }
        } else {
            // 블록이 비어있는 경우 표지판 설치
            signBlock.setType(Material.OAK_SIGN);
            BlockState state = signBlock.getState();
            if (state instanceof Sign) {
                Sign sign = (Sign) state;
                sign.setLine(1, playerName);
                sign.setLine(2, "여기에 잠들다.");
                sign.setGlowingText(true); // 발광 텍스트 적용
                sign.update();
            }
        }
    }
}
