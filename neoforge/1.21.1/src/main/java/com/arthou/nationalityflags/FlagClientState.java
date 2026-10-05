package com.arthou.nationalityflags;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FlagClientState {
    private static final Map<UUID, List<String>> FLAGS_BY_PLAYER = new ConcurrentHashMap<>();

    private FlagClientState() {
    }

    public static void setPlayerFlags(UUID playerId, List<String> countries) {
        if (countries == null || countries.isEmpty()) {
            FLAGS_BY_PLAYER.remove(playerId);
        } else {
            FLAGS_BY_PLAYER.put(playerId, List.copyOf(countries));
        }
        FlagHiddenNamesBridge.sync();
    }

    public static List<String> flagsFor(UUID playerId) {
        return FLAGS_BY_PLAYER.getOrDefault(playerId, List.of());
    }

    public static boolean isSelected(UUID playerId, String countryId) {
        return flagsFor(playerId).contains(countryId);
    }

    public static Component prefixFor(UUID playerId) {
        Minecraft minecraft = Minecraft.getInstance();
        long gameTime = minecraft.level == null ? 0L : minecraft.level.getGameTime();
        return FlagData.prefixFor(flagsFor(playerId), gameTime);
    }

    public static Component decorate(UUID playerId, Component name) {
        return FlagData.decorate(prefixFor(playerId), name);
    }

    public static Component decorate(Player player, Component name) {
        return player == null ? name : decorate(player.getUUID(), name);
    }

    public static void clear() {
        FLAGS_BY_PLAYER.clear();
    }

    static Map<UUID, List<String>> snapshot() {
        return Map.copyOf(FLAGS_BY_PLAYER);
    }
}
