package org.enteras.project_lostar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.bukkit.ChatColor;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class aimlessEmote implements Listener, CommandExecutor {

    private final List<String> emoteList = Arrays.asList("angry", "heart", "ㅗ", "damage", "critical", "spit", "no", "note", "rage", "tear", "lava", "honey");
    private final Map<String, EmoteAction> emotes = new HashMap<>();

    public aimlessEmote() {
        register("angry", "화남", location -> {
            location.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, location, 0, 0.0, 0.0, 0.0, 0.0);
        });

        register("heart", "하트", location -> {
            Location particleLocation = location.clone().add(0, 2, 0); // 현재 위치에서 2 블록 위의 위치를 얻습니다.
            location.getWorld().spawnParticle(Particle.HEART, particleLocation, 0, 0.0, 0.0, 0.0, 0.0);
        });

        register("ㅗ", "망할", location -> {
            location.getWorld().spawnParticle(Particle.HEART, location, 0, 0.0, 0.0, 0.0, 0.0);
        });

        register("damage", "상처", location -> {
            location.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, location, 0, 0.0, 0.0, 0.0, 0.0);
        });

        register("critical", "많은상처", location -> {
            location.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, location, 16, 0.5, 0.5, 0.5, 0.0);
        });

        register("spit", "퉤", location -> {
            Vector v = location.getDirection();
            location.getWorld().spawnParticle(Particle.SPIT, location, 0, v.getX(), v.getY(), v.getZ(), 1.0);
            location.getWorld().playSound(location, Sound.ENTITY_LLAMA_SPIT, 1.0F, 1.0F);
        });

        register("no", "하지마", location -> {
            location.getWorld().spawnParticle(Particle.BLOCK_MARKER, location, 0, 0.0, 0.0, 0.0, 0.0);
            location.getWorld().playSound(location, Sound.BLOCK_ANVIL_LAND, 0.5F, 0.1F);
        });

        register("note", "즐", location -> {
            location.getWorld().spawnParticle(Particle.NOTE, location, 0, 0.0, 0.0, 0.0, 1.0);
        });

        register("rage", "열받", location -> {
            location.getWorld().spawnParticle(Particle.LAVA, location, 128, 0.0, 0.0, 0.0, 1.0);
            location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.2F);
        });

        register("tear", "눈물", location -> {
            for (int i = 0; i <= 1; i++) {
                Vector v = new Vector(-0.1 + i * 0.2, 0.1, 0.4);
                v.rotateAroundX(Math.toRadians(location.getPitch())).rotateAroundY(Math.toRadians(-location.getYaw()));

                location.getWorld().spawnParticle(Particle.FALLING_WATER, location.getX() + v.getX(), location.getY() + 1.3 + v.getY(), location.getZ() + v.getZ(), 0, 0.0, 0.0, 0.0, 1.0);
            }
        });

        register("lava", "피눈물", location -> {
            for (int i = 0; i <= 1; i++) {
                Vector v = new Vector(-0.1 + i * 0.2, 0.1, 0.4);
                v.rotateAroundX(Math.toRadians(location.getPitch())).rotateAroundY(Math.toRadians(-location.getYaw()));

                location.getWorld().spawnParticle(Particle.FALLING_LAVA, location.getX() + v.getX(), location.getY() + 1.3 + v.getY(), location.getZ() + v.getZ(), 0, 0.0, 0.0, 0.0, 1.0);
            }
        });

        register("honey", "눈꼽", location -> {
            for (int i = 0; i <= 1; i++) {
                Vector v = new Vector(-0.1 + i * 0.2, 0.1, 0.4);
                v.rotateAroundX(Math.toRadians(location.getPitch())).rotateAroundY(Math.toRadians(-location.getYaw()));

                location.getWorld().spawnParticle(Particle.FALLING_HONEY, location.getX() + v.getX(), location.getY() + 1.3 + v.getY(), location.getZ() + v.getZ(), 0, 0.0, 0.0, 0.0, 1.0);
            }
        });

    }

    private void register(String name, String subname, EmoteAction emoteAction) {
        emotes.put(name, emoteAction);
        emotes.put(subname, emoteAction);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Location playerLocation = event.getPlayer().getLocation();
        EmoteAction emoteAction = emotes.get(event.getAction().name().toLowerCase());
        if (emoteAction != null) {
            emoteAction.perform(playerLocation);
        }
    }

    private interface EmoteAction {
        void perform(Location location);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("emote")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                // 이모티콘 목록을 보여줍니다.
                StringBuilder emoteListString = new StringBuilder();
                for (String emote : emoteList) {
                    emoteListString.append(emote).append(", ");
                }
                if (emoteListString.length() > 0) {
                    emoteListString.delete(emoteListString.length() - 2, emoteListString.length());
                }
                sender.sendMessage(ChatColor.YELLOW + "Available emotes: " + emoteListString.toString());
                return true;
            } else if (args.length == 1) {
                String partialName = args[0].toLowerCase();
                List<String> matchedEmotes = new ArrayList<>();
                for (String emote : emoteList) {
                    if (emote.startsWith(partialName)) {
                        matchedEmotes.add(emote);
                    }
                }
                if (matchedEmotes.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "No matching emotes found.");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Matched emotes: " + String.join(", ", matchedEmotes));
                }
                return true;
            }

            String emoteName = args[0];
            EmoteAction emoteAction = emotes.get(emoteName.toLowerCase());
            if (emoteAction != null) {
                emoteAction.perform(player.getLocation());
            } else {
                sender.sendMessage("Unknown emote: " + emoteName);
            }
            return true;
        }
        return false;
    }
}
