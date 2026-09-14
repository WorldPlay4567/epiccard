package com.epiccard.payload;

import com.epiccard.EpicCard;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record BattleCloseC2SPayload(int entity) implements CustomPayload {

    public static final Identifier BATTLE_START_PAYLOAD_ID = EpicCard.id("battle_close_payload_id");
    public static final CustomPayload.Id<BattleCloseC2SPayload> ID = new CustomPayload.Id<>(BATTLE_START_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, BattleCloseC2SPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, BattleCloseC2SPayload::entity, BattleCloseC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
