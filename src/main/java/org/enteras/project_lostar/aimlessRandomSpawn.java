package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class aimlessRandomSpawn implements Listener {

    private final JavaPlugin plugin;
    private final Map<UUID, Boolean> firstSpawnMap;
    private final FileConfiguration config;

    public aimlessRandomSpawn(JavaPlugin plugin) {
        this.plugin = plugin;
        this.firstSpawnMap = new HashMap<>();
        this.config = plugin.getConfig();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // 플레이어의 최초 스폰 여부 확인
        if (firstSpawnMap.getOrDefault(uuid, true)) {
            // 최초 스폰이라면 랜덤한 위치로 스폰
            spawnPlayerRandomly(player);

            // 최초 스폰 여부를 false로 변경하여 다음에는 랜덤 스폰을 수행하지 않도록 함
            firstSpawnMap.put(uuid, false);
        } else {
            // 최초 스폰이 아니라면 이전에 저장된 로그아웃 위치로 스폰
            Location logoutLocation = player.getLocation();
            player.teleport(logoutLocation);
        }
    }

    private void spawnPlayerRandomly(Player player) {
        World world = player.getWorld();

        // 랜덤한 x, z 좌표 생성 (-10000 ~ 10000 범위 내)
        double randomX = -10000 + (Math.random() * 20001);
        double randomZ = -10000 + (Math.random() * 20001);
        double y = world.getHighestBlockYAt((int) randomX, (int) randomZ);

        Location spawnLocation = new Location(world, randomX, y, randomZ);
        player.teleport(spawnLocation);
    }
}
