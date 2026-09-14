package com.epiccard.payload;

import com.epiccard.EpicCard;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record InteractionC2SPayload(int interaction) implements CustomPayload {

    public static final Identifier INTERACTION_PAYLOAD_ID = EpicCard.id("interaction");
    public static final CustomPayload.Id<InteractionC2SPayload> ID = new CustomPayload.Id<>(INTERACTION_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, InteractionC2SPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, InteractionC2SPayload::interaction, InteractionC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
