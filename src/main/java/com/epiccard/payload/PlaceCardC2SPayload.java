package com.epiccard.payload;

import com.epiccard.EpicCard;
import com.epiccard.game.MainCardGame;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PlaceCardC2SPayload(int x, int id_card) implements CustomPayload {

    public static final Identifier PLACE_CARD_PAYLOAD_ID = EpicCard.id("place_card_payload_id");
    public static final CustomPayload.Id<PlaceCardC2SPayload> ID = new CustomPayload.Id<>(PLACE_CARD_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, PlaceCardC2SPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, PlaceCardC2SPayload::x,
                    PacketCodecs.INTEGER, PlaceCardC2SPayload::id_card, PlaceCardC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
