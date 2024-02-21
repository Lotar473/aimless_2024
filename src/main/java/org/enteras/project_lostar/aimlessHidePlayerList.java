package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class aimlessHidePlayerList implements Runnable, Listener {

    private static boolean update = false;

    public static void update() {
        update = true;
    }

    @Override
    public void run() {
        if (update) {
            update = false;
            updatePlayerList();
        }
    }

    private void updatePlayerList() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        List<PlayerInfoData> list = new ArrayList<>();

        for (org.bukkit.OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            WrappedGameProfile profile;
            if (offlinePlayer instanceof Player) {
                profile = WrappedGameProfile.fromPlayer((Player) offlinePlayer);
            } else {
                profile = WrappedGameProfile.fromOfflinePlayer(offlinePlayer).withName(offlinePlayer.getName());
            }

            list.add(new PlayerInfoData(
                    profile,
                    0,
                    EnumWrappers.NativeGameMode.NOT_SET,
                    WrappedChatComponent.fromText(offlinePlayer.getName())
            ));
        }

        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        packet.getPlayerInfoDataLists().write(0, list);

        com.comphenix.protocol.ProtocolManager pm = ProtocolLibrary.getProtocolManager();

        for (Player player : Bukkit.getOnlinePlayers()) {
            pm.sendServerPacket(player, packet);
        }
    }
}
