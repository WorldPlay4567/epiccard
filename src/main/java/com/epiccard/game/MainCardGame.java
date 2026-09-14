package com.epiccard.game;

import com.epiccard.network.NetworkCardGame;
import com.epiccard.payload.TakeCardSyncS2CPayload;
import com.epiccard.register.CardBattle;
import com.epiccard.register.CardRegister;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MainCardGame {

    public final UUID uuidTable;
    public final UUID uuidEnemy;
    public final UUID uuidPlayer;

    public CardBattle[][] TABLE = new CardBattle[4][3];
    public List<CardBattle> IN_HAND = new ArrayList<>();

    public List<CardBattle> CARD_INVENTORY = new ArrayList<>();

    public MainCardGame(UUID uuidTable, UUID uuidEnemy, UUID uuid) {
        this.uuidTable = uuidTable;
        this.uuidEnemy = uuidEnemy;
        this.uuidPlayer = uuid;
    }

    public void tick(MinecraftServer server) {
        Entity entity = server.getOverworld().getEntity(uuidEnemy);
        Entity tableEntity = server.getOverworld().getEntity(uuidTable);
        if(entity != null && tableEntity != null) {
            entity.setNoGravity(true);
            double eye = entity.getStandingEyeHeight();
            entity.setPosition(tableEntity.getPos().add(-1.2,1.49 - eye ,0));
            entity.setHeadYaw(275);
            entity.setAngles(275, 0);
        }
    }

    public void startBattle(MinecraftServer server) {

        if(true) {return;}

        for(int x = 0; x < 3; x++) {
            TABLE[x][TableLine.PLAYER_LINE.ordinal()].cardAttack();
        }
        for(int x = 0; x < 3; x++) {
            TABLE[x][TableLine.WAIT_LINE.ordinal()].cardAttack();
        }
        for(int x = 0; x < 3; x++) {
            TABLE[x][TableLine.ENEMY_LINE.ordinal()].cardAttack();
        }
    }

    @Nullable
    public CardBattle getCardInHand(int id) {
        for(CardBattle cardBattle : IN_HAND) {
            if(cardBattle.id == id) {
                return cardBattle;
            }
        }
        return null;
    }



    public boolean setCardBattle(int x, int line, CardBattle card) {
        if(x < 0 || x > 3) return false;
        if(line < 0 || line > 2) return false;

        if(TABLE[x][line] != null) return false;

        TABLE[x][line] = card;

        return true;
    }

    public void takeCard(MinecraftServer server) {

    }

    public void takeAnimal(MinecraftServer server) {

        CardBattle cardBattle = new CardBattle(CardRegister.CARD_ANIMAL);
        IN_HAND.add(cardBattle);
        TakeCardSyncS2CPayload takeCardSyncS2CPayload = new TakeCardSyncS2CPayload(cardBattle.toNbt());
        if(server.getPlayerManager().getPlayer(uuidPlayer) != null) {
            ServerPlayNetworking.send(server.getPlayerManager().getPlayer(uuidPlayer), takeCardSyncS2CPayload);
        }
    }



    public boolean setCardBattle(int x, TableLine tableLine, CardBattle card) {
        return setCardBattle(x, tableLine.ordinal(), card);
    }

    public void setCardBattleAtHand(int x, int id_card, ServerPlayerEntity player) {
        NetworkCardGame.removeCard(id_card, player);
        CardBattle cardBattle = getCardInHand(id_card);
        this.setCardBattle(x, TableLine.PLAYER_LINE, cardBattle);
        IN_HAND.remove(cardBattle);
    }

    public enum TableLine {
        PLAYER_LINE, ENEMY_LINE, WAIT_LINE
    }

}
