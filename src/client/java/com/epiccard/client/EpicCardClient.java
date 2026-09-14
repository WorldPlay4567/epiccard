package com.epiccard.client;

import com.epiccard.client.entity.ClientEntityRegister;
import com.epiccard.client.game.TableManager;
import com.epiccard.client.render.TableManagerRender;
import com.epiccard.client.render.TableRender;
import com.epiccard.client.screen.BattleScreen;
import com.epiccard.entity.entity.TableEntity;
import com.epiccard.payload.BattleStartS2CPayload;
import com.epiccard.payload.RemoveCardS2CPayload;
import com.epiccard.payload.TakeCardSyncS2CPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.ArrayList;
import java.util.List;

public class EpicCardClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

        ClientEntityRegister.init();
        TableManagerRender.init();
        registerPayloadClient();
        ClientEntityEvents.ENTITY_LOAD.register(EpicCardClient::entityLoad);
        ClientEntityEvents.ENTITY_UNLOAD.register(EpicCardClient::entityUnload);
        ModelsRegister.registerModels();

	}




    /// TODO
    /// Это место может поломаться, без нужных проверок
    private void registerPayloadClient() {
        ClientPlayNetworking.registerGlobalReceiver(BattleStartS2CPayload.ID, (t,context) -> {
            context.client().execute(() -> {
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                MinecraftClient.getInstance().setScreen(new BattleScreen("test",(TableEntity) player.getWorld().getEntityById(t.entity())));
            });
        });


        ClientPlayNetworking.registerGlobalReceiver(TakeCardSyncS2CPayload.ID, (t, context) -> {
           context.client().execute(()-> {
                if(MinecraftClient.getInstance().currentScreen instanceof BattleScreen battleScreen) {
                    battleScreen.addCard(t.nbtCompound());
                }
           });
        });

        ClientPlayNetworking.registerGlobalReceiver(RemoveCardS2CPayload.ID, (t, context) -> {
           context.client().execute(()-> {
               if(MinecraftClient.getInstance().currentScreen instanceof BattleScreen battleScreen) {
                   battleScreen.removeCard(t.id());
               }
           });
        });
    }

    private static void entityUnload(Entity entity, ClientWorld clientWorld) {
        if(entity instanceof TableEntity tableEntity) {
            TableManagerRender.removeTable(tableEntity);
            TableManager.removeTableEntity(tableEntity);
        }
    }

    private static void entityLoad(Entity entity, ClientWorld clientWorld) {
        if(entity instanceof TableEntity tableEntity) {
            TableManagerRender.addTable(new TableRender(tableEntity));
            TableManager.addTableEntity(tableEntity);
        }
    }
}