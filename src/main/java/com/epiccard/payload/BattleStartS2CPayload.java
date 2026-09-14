package com.epiccard.payload;

import com.epiccard.EpicCard;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record BattleStartS2CPayload(int entity) implements CustomPayload {

    public static final Identifier BATTLE_START_PAYLOAD_ID = EpicCard.id("battle_start_payload_id");
    public static final CustomPayload.Id<BattleStartS2CPayload> ID = new CustomPayload.Id<>(BATTLE_START_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, BattleStartS2CPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, BattleStartS2CPayload::entity, BattleStartS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
