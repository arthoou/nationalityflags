package com.arthou.nationalityflags;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public final class FlagServerEvents {
    private static int refreshTicker;

    private FlagServerEvents() {
    }

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        clearLegacyFlagsForOnlinePlayers(player);
        syncAllStatesTo(player);
    }

    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            FlagNetwork.broadcast(player);
        }
    }

    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            refreshPlayer(player);
            FlagNetwork.broadcast(player);
        }
    }

    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            refreshPlayer(player);
            FlagNetwork.broadcast(player);
        }
    }

    public static void onPlayerClone(PlayerEvent.Clone event) {
        FlagData.clearLegacyFlags(event.getOriginal());
        FlagData.clearLegacyFlags(event.getEntity());
        for (String tag : event.getOriginal().getTags()) {
            if (tag.startsWith(FlagData.FLAG_TAG_PREFIX)) {
                event.getEntity().addTag(tag);
            }
        }
        if (event.getEntity() instanceof ServerPlayer player) {
            refreshPlayer(player);
            FlagNetwork.broadcast(player);
        }
    }

    public static void onTabListNameFormat(PlayerEvent.TabListNameFormat event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        Component base = event.getDisplayName() == null
            ? PlayerTeam.formatNameForTeam(player.getTeam(), FlagHiddenNamesServerBridge.displayName(player))
            : event.getDisplayName();
        event.setDisplayName(FlagData.decorate(player, base));
    }

    public static void onServerTick(ServerTickEvent.Post event) {
        refreshTicker++;
        if (refreshTicker < 40) {
            return;
        }
        refreshTicker = 0;

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            if (FlagData.selectedCountries(player).size() > 1) {
                refreshPlayer(player);
                FlagNetwork.broadcast(player);
            }
        }
    }

    public static void refreshPlayer(ServerPlayer player) {
        player.refreshDisplayName();
        player.refreshTabListName();
        player.server.getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player));
    }

    private static void syncAllStatesTo(ServerPlayer recipient) {
        for (ServerPlayer onlinePlayer : recipient.server.getPlayerList().getPlayers()) {
            FlagNetwork.syncToPlayer(recipient, onlinePlayer.getUUID(), FlagData.selectedCountries(onlinePlayer));
        }
    }

    private static void clearLegacyFlagsForOnlinePlayers(ServerPlayer trigger) {
        for (ServerPlayer onlinePlayer : trigger.server.getPlayerList().getPlayers()) {
            FlagData.clearLegacyFlags(onlinePlayer);
            refreshPlayer(onlinePlayer);
            FlagNetwork.broadcast(onlinePlayer);
        }
    }
}
