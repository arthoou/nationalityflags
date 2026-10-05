package com.arthou.nationalityflags;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundToggleFlagPacket(String countryId) implements CustomPacketPayload {
    public static final Type<ServerboundToggleFlagPacket> TYPE = new Type<>(NationalityFlags.id("toggle_flag"));
    public static final StreamCodec<FriendlyByteBuf, ServerboundToggleFlagPacket> STREAM_CODEC = StreamCodec.of(
        (buffer, packet) -> buffer.writeUtf(packet.countryId),
        buffer -> new ServerboundToggleFlagPacket(buffer.readUtf())
    );

    @Override
    public Type<ServerboundToggleFlagPacket> type() {
        return TYPE;
    }

    public static void handle(ServerboundToggleFlagPacket packet, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            FlagData.toggleCountry(player, packet.countryId);
            FlagServerEvents.refreshPlayer(player);
            FlagNetwork.broadcast(player);
        }
    }
}
