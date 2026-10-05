package com.arthou.nationalityflags;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;
import java.util.UUID;

public final class FlagNetwork {
    private static final String PROTOCOL_VERSION = "1";

    private FlagNetwork() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToClient(ClientboundFlagStatePacket.TYPE, ClientboundFlagStatePacket.STREAM_CODEC, ClientboundFlagStatePacket::handle);
        registrar.playToServer(ServerboundToggleFlagPacket.TYPE, ServerboundToggleFlagPacket.STREAM_CODEC, ServerboundToggleFlagPacket::handle);
    }

    public static void toggleFlag(String countryId) {
        PacketDistributor.sendToServer(new ServerboundToggleFlagPacket(countryId));
    }

    public static void syncToPlayer(ServerPlayer recipient, UUID playerId, List<String> countries) {
        PacketDistributor.sendToPlayer(recipient, new ClientboundFlagStatePacket(playerId, countries));
    }

    public static void broadcast(ServerPlayer changedPlayer) {
        List<String> selected = FlagData.selectedCountries(changedPlayer);
        for (ServerPlayer recipient : changedPlayer.server.getPlayerList().getPlayers()) {
            syncToPlayer(recipient, changedPlayer.getUUID(), selected);
        }
    }
}
