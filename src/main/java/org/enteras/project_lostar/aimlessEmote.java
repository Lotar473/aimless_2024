package org.enteras.project_lostar;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.bukkit.command.TabCompleter;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class aimlessEmote implements Listener, CommandExecutor, TabCompleter {

    private final Map<String, EmoteAction> emotes = new HashMap<>();

    public aimlessEmote() {
        register("angry", "화남", location -> {
            Location particleLocation = location.clone().add(0, 2, 0);
            location.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, particleLocation, 0, 0.0, 0.0, 0.0, 0.0);
        });

        register("heart", "하트", location -> {
            Location particleLocation = location.clone().add(0, 2, 0); // 현재 위치에서 2 블록 위의 위치를 얻습니다.
            location.getWorld().spawnParticle(Particle.HEART, particleLocation, 0, 0.0, 0.0, 0.0, 0.0);
        });

        register("ㅗ", "망할", location -> {
            Location particleLocation = location.clone().add(0, 2, 0); // 현재 위치에서 2 블록 위의 위치를 얻습니다.
            location.getWorld().spawnParticle(Particle.HEART, particleLocation, 0, 0.0, 0.0, 0.0, 0.0);
        });

        register("damage", "상처", location -> {
            Location particleLocation = location.clone().add(0, 2, 0); // 현재 위치에서 2 블록 위의 위치를 얻습니다.
            location.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, particleLocation, 0, 0.0, 0.0, 0.0, 0.0);
        });

        register("critical", "많은상처", location -> {
            location.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, location, 16, 0.5, 0.5, 0.5, 0.0);
        });

        register("spit", "퉤", location -> {
            Location particleLocation = location.clone().add(0, 1.45, 0);
            Vector v = location.getDirection();
            location.getWorld().spawnParticle(Particle.SPIT, particleLocation, 0, v.getX(), v.getY(), v.getZ(), 1.0);
            location.getWorld().playSound(location, Sound.ENTITY_LLAMA_SPIT, 1.0F, 1.0F);
        });

        register("no", "하지마", location -> {
            Location particleLocation = location.clone().add(0, 2, 0);
            location.getWorld().spawnParticle(Particle.BLOCK_MARKER, particleLocation, 0, 0.0, 0.0, 0.0, 0.0, Bukkit.createBlockData(Material.BARRIER));
            location.getWorld().playSound(location, Sound.BLOCK_ANVIL_LAND, 0.5F, 0.1F);
        });

        register("note", "즐", location -> {
            Location particleLocation = location.clone().add(0, 2, 0); // 현재 위치에서 2 블록 위의 위치를 얻습니다.
            location.getWorld().spawnParticle(Particle.NOTE, particleLocation, 0, 0.0, 0.0, 0.0, 1.0);
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

        register("gratz", "경축", location -> {
            for (int i = 0; i <= 1; i++) {
                Vector v = new Vector(-0.1 + i * 0.2, -0.5, 0.4);
                v.rotateAroundX(Math.toRadians(location.getPitch())).rotateAroundY(Math.toRadians(-location.getYaw()));

                FireworkEffect effect = FireworkEffect.builder()
                        .flicker(true)
                        .withColor(Color.RED)
                        .withColor(Color.ORANGE)
                        .withColor(Color.YELLOW)
                        .withColor(Color.GREEN)
                        .withColor(Color.BLUE)
                        .withColor(Color.PURPLE)
                        .build();

                // 폭죽 생성
                Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
                FireworkMeta meta = (FireworkMeta) firework.getFireworkMeta(); // 메타데이터 가져오기
                meta.addEffect(effect);
                firework.setFireworkMeta(meta);
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
                sender.sendMessage("플레이어만 이 명령어를 사용할 수 있습니다.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("사용법: /emote <emote>");
                return true;
            }

            Player player = (Player) sender;
            String emoteName = args[0];
            EmoteAction emoteAction = emotes.get(emoteName.toLowerCase());
            if (emoteAction != null) {
                emoteAction.perform(player.getLocation());
            } else {
                sender.sendMessage("알 수 없는 이모트: " + emoteName);
            }
            return true;
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("emote")) {
            if (args.length == 1) {
                String input = args[0].toLowerCase();
                List<String> completions = new ArrayList<>();
                for (String emote : emotes.keySet()) {
                    if (emote.startsWith(input)) {
                        completions.add(emote);
                    }
                }
                return completions;
            }
        }
        return null;
    }
}
