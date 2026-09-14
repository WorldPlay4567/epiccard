package com.epiccard;

import com.epiccard.entity.EntityRegister;
import com.epiccard.entity.entity.TableEntity;
import com.epiccard.game.CardWorld;
import com.epiccard.game.MainCardGame;
import com.epiccard.payload.*;
import com.epiccard.register.CardRegister;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EpicCard implements ModInitializer {
	public static final String MOD_ID = "epiccard";
    public static int ID = 0;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static CardWorld CARDWORLD;


	@Override
	public void onInitialize() {
        EntityRegister.init();
        registerPayLoad();

        ServerTickEvents.END_SERVER_TICK.register(EpicCard::tick);

        ServerLifecycleEvents.SERVER_STARTED.register(EpicCard::startWorld);
        ServerLifecycleEvents.SERVER_STOPPED.register(EpicCard::closeWorld);
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult ) -> {
            if(player instanceof ServerPlayerEntity serverPlayerEntity) {
                TableEntity tableEntity = new TableEntity(EntityRegister.TABLE_ENTITY, world);
                tableEntity.setPos(player.getX(), player.getY(), player.getZ());
                world.spawnEntity(tableEntity);

                BattleStartS2CPayload battleStartS2CPayload = new BattleStartS2CPayload(tableEntity.getId());
                ServerPlayNetworking.send(serverPlayerEntity, battleStartS2CPayload);
                CARDWORLD.addMainCardGame(player.getUuid(), tableEntity, entity);
            }

            return ActionResult.PASS;
        });

        CardRegister.init();
        registerPayloadServer();
		LOGGER.info("Hello Fabric world!");
	}

    private static void closeWorld(MinecraftServer server) {
        CARDWORLD = null;
    }

    private static void startWorld(MinecraftServer server) {
        CARDWORLD = new CardWorld();
    }

    private static void tick(MinecraftServer server) {
        CARDWORLD.tick(server);
    }

    /// TODO
    /// Это место может поломаться, без нужных проверок
    @SuppressWarnings("resource")
    private void registerPayloadServer() {
        ServerPlayNetworking.registerGlobalReceiver(BattleCloseC2SPayload.ID, (t ,context) -> {
            context.server().execute(() -> {
                Entity entity = context.server().getOverworld().getEntityById(t.entity());
                Entity entity1 = context.server().getOverworld().getEntity(CARDWORLD.getGame(context.player().getUuid()).uuidEnemy);
                if(entity1 != null) {
                    entity1.setNoGravity(false);
                    entity.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);
                }
                EpicCard.CARDWORLD.removeMainCardGame(context.player().getUuid());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(PlaceCardC2SPayload.ID, (t, context) -> {
            MainCardGame cardGame = EpicCard.CARDWORLD.getGame(context.player().getUuid());
            context.server().execute(() -> {
                cardGame.setCardBattleAtHand(t.x(), t.id_card(), context.player());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(InteractionC2SPayload.ID, (t, context) -> {
            EpicCard.CARDWORLD.interaction(t.interaction(), context.player().getUuid(), context.server());
        });


    }

    public int getID() {
        int _id = ID;
        ID++;
        return _id;
    }

    public void registerPayLoad() {

        PayloadTypeRegistry.playS2C().register(BattleStartS2CPayload.ID, BattleStartS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(BattleCloseC2SPayload.ID, BattleCloseC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(InteractionC2SPayload.ID, InteractionC2SPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(PlaceCardC2SPayload.ID, PlaceCardC2SPayload.CODEC);


        PayloadTypeRegistry.playS2C().register(TakeCardSyncS2CPayload.ID, TakeCardSyncS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(RemoveCardS2CPayload.ID, RemoveCardS2CPayload.CODEC);

    }

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
