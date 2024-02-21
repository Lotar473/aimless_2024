package org.enteras.project_lostar;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.LightningStrike;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Effect;

public class ChestBombCommandExecutor implements CommandExecutor, Listener {
    private boolean explosive = false;
    private final DungPlugin plugin;

    public ChestBombCommandExecutor(DungPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin); // Listener 등록
    }

    public void setExplosive(boolean value) {
        explosive = value;
    }

    private void explodeChest(Player player) {
        if (explosive) {
            Location location = player.getLocation();
            World world = player.getWorld();

            // 플레이어에게 저항 2 효과 주기
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 5));

            // 파티클 숨기기
            player.spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 0, 0, 0, 0, 0);

            // 번개 소환
            world.strikeLightning(location);

            // 번개에 의한 폭발
            world.createExplosion(location.getX(), location.getY(), location.getZ(), 3, true, true);
            spawnRandomMonster2(location, world);
        }
    }

    private void spawnRandomMonster(Location location, World world) {
        EntityType[] monsters = { EntityType.WARDEN, EntityType.WITHER, EntityType.ELDER_GUARDIAN };
        EntityType randomMonster = monsters[(int) (Math.random() * monsters.length)];

        world.spawnEntity(location, randomMonster);
    }
    private void spawnRandomMonster2(Location location, World world) {
        EntityType[] monsters = { EntityType.ZOGLIN, EntityType.PIGLIN_BRUTE, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.RAVAGER };
        EntityType randomMonster = monsters[(int) (Math.random() * monsters.length)];

        world.spawnEntity(location, randomMonster);
    }

    private void spawnRandomMonster3(Location location, World world) {
        EntityType[] monsters = { EntityType.VEX, EntityType.GUARDIAN, EntityType.WITHER_SKELETON, EntityType.WITCH };
        EntityType randomMonster = monsters[(int) (Math.random() * monsters.length)];

        world.spawnEntity(location, randomMonster);
    }

    private void spawnRandomMonster4(Location location, World world) {
        EntityType[] monsters = { EntityType.ENDERMAN, EntityType.ZOMBIE, EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.CREEPER,
                EntityType.WITCH, EntityType.MAGMA_CUBE, EntityType.SLIME };
        EntityType randomMonster = monsters[(int) (Math.random() * monsters.length)];

        world.spawnEntity(location, randomMonster);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        // 상자를 우클릭 했을 때
        if (explosive && event.getAction().toString().contains("RIGHT") && clickedBlock != null && clickedBlock.getType() == Material.CHEST) {
            explodeChest(player);
            spawnRandomMonster2(player.getLocation(), player.getWorld());
            spawnRandomMonster3(player.getLocation(), player.getWorld());
            spawnRandomMonster4(player.getLocation(), player.getWorld());
            player.sendMessage("그럴 줄 알았지");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack currentItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        // 상자를 조합 했을 때
        if (explosive && clickedInventory != null && currentItem != null && currentItem.getType() == Material.CHEST) {
            player.sendMessage("그럴 줄 알았지");
            explodeChest(player);
            spawnRandomMonster2(player.getLocation(), player.getWorld());
            spawnRandomMonster3(player.getLocation(), player.getWorld());
            spawnRandomMonster4(player.getLocation(), player.getWorld());
            event.setCurrentItem(null);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1 && args[0].equalsIgnoreCase("4732")) {
            if (explosive) {
                player.sendMessage("상자 폭발 기능이 이미 켜져 있습니다.");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.5f);
            } else {
                setExplosive(true);
                player.sendMessage(ChatColor.GREEN + "상자 폭발 기능이 활성화되었습니다.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("0473")) {
            if (!explosive) {
                player.sendMessage("상자 폭발 기능이 이미 꺼져 있습니다.");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.5f);
            } else {
                setExplosive(false);
                player.sendMessage(ChatColor.GREEN + "상자 폭발 기능이 비활성화되었습니다.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
            }
            return true;
        } else {
            player.sendMessage("올바른 명령어 사용법: /chestbomb <on/off>");
            return true;
        }
    }
}
