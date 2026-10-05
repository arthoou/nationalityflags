package com.arthou.nationalityflags;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ClientboundFlagStatePacket(UUID playerId, List<String> countries) implements CustomPacketPayload {
    public static final Type<ClientboundFlagStatePacket> TYPE = new Type<>(NationalityFlags.id("flag_state"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundFlagStatePacket> STREAM_CODEC = StreamCodec.of(
        (buffer, packet) -> {
            buffer.writeUUID(packet.playerId);
            buffer.writeVarInt(packet.countries.size());
            for (String country : packet.countries) {
                buffer.writeUtf(country);
            }
        },
        buffer -> {
            UUID playerId = buffer.readUUID();
            int size = buffer.readVarInt();
            List<String> countries = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                countries.add(buffer.readUtf());
            }
            return new ClientboundFlagStatePacket(playerId, countries);
        }
    );

    @Override
    public Type<ClientboundFlagStatePacket> type() {
        return TYPE;
    }

    public static void handle(ClientboundFlagStatePacket packet, IPayloadContext context) {
        FlagClientState.setPlayerFlags(packet.playerId, packet.countries);
    }
}
