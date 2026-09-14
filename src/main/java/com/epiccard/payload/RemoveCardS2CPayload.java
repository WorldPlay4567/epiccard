package com.epiccard.payload;

import com.epiccard.EpicCard;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RemoveCardS2CPayload(int id) implements CustomPayload{
    public static final Identifier REMOVE_CARD_PAYLOAD_ID = EpicCard.id("remove_card_payload");
    public static final CustomPayload.Id<RemoveCardS2CPayload> ID = new CustomPayload.Id<>(REMOVE_CARD_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, RemoveCardS2CPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, RemoveCardS2CPayload::id, RemoveCardS2CPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
