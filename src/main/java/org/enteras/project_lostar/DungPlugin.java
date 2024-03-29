package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class DungPlugin extends JavaPlugin {

    private InventoryChangePlugin inventoryChangePlugin;
    private ChestBombCommandExecutor chestBombCommandExecutor;
    private aimlessHidePlayerList aimlessHidePlayerList;

    @Override
    public void onEnable() {
        getLogger().info("LostaR 플러그인이 활성화되었습니다!");

        this.inventoryChangePlugin = new InventoryChangePlugin(this);
        this.chestBombCommandExecutor = new ChestBombCommandExecutor(this);
        this.aimlessHidePlayerList = new aimlessHidePlayerList(); // 추가: aimlessHidePlayerList 인스턴스 생성

        getCommand("dung").setExecutor(new DungCommandExecutor());
        getCommand("mondstadt").setExecutor(new MondstadtCommandExecutor());
        getCommand("hub").setExecutor(new HubCommandExecutor());
        getCommand("boatrace").setExecutor(new BoatRaceCommandExecutor());
        getCommand("pee").setExecutor(new PeeCommandExecutor());
        getCommand("autoc").setExecutor(new AutocCommandExecutor());
        getCommand("soonbo").setExecutor(new SoonboCommandExecutor(this));
        getCommand("chestbomb").setExecutor(chestBombCommandExecutor);
        getCommand("aimless").setExecutor(new aimlessCommandExecutor(this));
        getServer().getPluginManager().registerEvents(new aimlessEmote(), this);
        getCommand("emote").setExecutor(new aimlessEmote());
        getCommand("emotelist").setExecutor(new aimlessEmoteList());
        getCommand("aimlesstitle").setExecutor(new aimlessPrestige(this));
        aimlessRandomSpawn randomSpawn = new aimlessRandomSpawn(this);
        randomSpawn.init();

        InventoryChangeCommandExecutor inventoryChangeExecutor = new InventoryChangeCommandExecutor(inventoryChangePlugin);
        getCommand("inventorychange").setExecutor(inventoryChangeExecutor);
        this.aimlessHidePlayerList = new aimlessHidePlayerList(); // aimlessHidePlayerList 객체 생성
        getServer().getPluginManager().registerEvents(aimlessHidePlayerList, this); // aimlessHidePlayerList 리스너 등록
    }
    //진서야 사랑해
    @Override
    public void onDisable() {
        getLogger().info("LostaR 플러그인이 비활성화되었습니다!");
    }

    private void scheduleRandomTask() {
        int initialDelay = 2400 + (int) (Math.random() * 9600); // 2분(2400틱)부터 10분(12000틱) 사이의 랜덤한 시간
        int period = 2400 + (int) (Math.random() * 9600); // 2분(2400틱)부터 10분(12000틱) 사이의 랜덤한 시간

        Bukkit.getScheduler().runTaskLater(this, () -> {
            inventoryChangePlugin.changeInventories();

            scheduleRandomTask();
        }, initialDelay);
    }
}
