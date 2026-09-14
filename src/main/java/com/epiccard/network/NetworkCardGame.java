package com.epiccard.network;

import com.epiccard.payload.RemoveCardS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public class NetworkCardGame {


    public static void removeCard(int i, ServerPlayerEntity player) {
        RemoveCardS2CPayload removeCardS2CPayload = new RemoveCardS2CPayload(i);
        ServerPlayNetworking.send(player, removeCardS2CPayload);
    }

}
