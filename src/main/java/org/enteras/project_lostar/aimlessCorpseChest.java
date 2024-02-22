package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
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

        // 플레이어의 갑옷을 갑옷 거치대에 추가
        addArmorStand(player, chest, deathLocation);

        // 플레이어의 인벤토리 아이템을 상자에 추가
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                chestInventory.addItem(item);
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

    private void addArmorStand(Player player, Chest chest, Location location) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0.5, 1, 0.5), EntityType.ARMOR_STAND);
        armorStand.setVisible(true);
        armorStand.setSmall(false);
        armorStand.setBasePlate(false);
        armorStand.setArms(true);
        armorStand.setInvulnerable(false);

        // 갑옷을 갑옷 거치대에 장착
        armorStand.setHelmet(player.getInventory().getHelmet());
        armorStand.setChestplate(player.getInventory().getChestplate());
        armorStand.setLeggings(player.getInventory().getLeggings());
        armorStand.setBoots(player.getInventory().getBoots());

        // 플레이어의 갑옷 제거
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }
}
