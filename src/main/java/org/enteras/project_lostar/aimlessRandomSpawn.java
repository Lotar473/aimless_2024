package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class aimlessRandomSpawn implements Listener {

    private final JavaPlugin plugin;

    public aimlessRandomSpawn(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getConsoleSender().sendMessage("AimlessRandomSpawn plugin enabled.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()) return; // If player has played before, don't spawn them randomly

        // 플레이어가 최초로 서버에 접속했을 때 랜덤한 위치로 스폰되도록 설정
        spawnPlayerAtRandomLocation(player);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("randomspawn")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be executed by players.");
                return true;
            }

            Player player = (Player) sender;
            spawnPlayerAtRandomLocation(player);
            sender.sendMessage("You have been randomly spawned.");
            return true;
        }
        return false;
    }

    private void spawnPlayerAtRandomLocation(Player player) {
        World world = player.getWorld();
        double randomX = -10000 + (Math.random() * 20001); // Random X coordinate within -10000 and 10000
        double randomZ = -10000 + (Math.random() * 20001); // Random Z coordinate within -10000 and 10000
        double y = world.getHighestBlockYAt((int) randomX, (int) randomZ);

        Location spawnLocation = new Location(world, randomX, y, randomZ);
        player.teleport(spawnLocation);
    }
}
