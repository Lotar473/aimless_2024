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

        // 표지판 생성
        createSignAboveChest(chest, player.getName());

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
        Block currentBlock = location.getBlock();

        // 블록을 위로 탐색하여 공기가 아닌 첫 번째 블록을 찾습니다.
        while (currentBlock.getType() == Material.AIR && currentBlock.getY() > 0) {
            currentBlock = currentBlock.getRelative(BlockFace.DOWN);
        }

        // 상자를 생성합니다.
        currentBlock = currentBlock.getRelative(BlockFace.UP);
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
            sign.setLine(1, playerName); // 플레이어의 닉네임을 첫 번째 줄에 설정합니다.
            sign.setLine(2, "여기에 잠들다."); // 두 번째 줄에 문구를 설정합니다.
            sign.setGlowingText(true); // 흰색 먹물 효과를 적용합니다.
            sign.update();
        }
    }
}
