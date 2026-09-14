package com.epiccard.payload;

import com.epiccard.EpicCard;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TakeCardSyncS2CPayload(NbtCompound nbtCompound) implements CustomPayload {

    public static final Identifier CARD_SYNC_PAYLOAD_ID = EpicCard.id("card_sync_payload_id");
    public static final CustomPayload.Id<TakeCardSyncS2CPayload> ID = new CustomPayload.Id<>(CARD_SYNC_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, TakeCardSyncS2CPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.NBT_COMPOUND, TakeCardSyncS2CPayload::nbtCompound, TakeCardSyncS2CPayload::new);


    ///TODO Наша задача это сделать payload который будет отправлять взятие карты
    ///TODO Надо сделать чтобы с сервера на клиент отправлялась взятие карты и появлялась карта на доске
    ///
    ///
    ///

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
