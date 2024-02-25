package org.enteras.project_lostar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class aimlessHidePlayerList implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null); // 입장 메시지를 null로 설정하여 아무런 메시지도 표시되지 않도록 함
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null); // 퇴장 메시지를 null로 설정하여 아무런 메시지도 표시되지 않도록 함
    }

    public static void register(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new aimlessHidePlayerList(), plugin);
    }
}
