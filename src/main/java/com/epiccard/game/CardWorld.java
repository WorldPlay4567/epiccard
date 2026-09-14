package com.epiccard.game;

import com.epiccard.entity.entity.TableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CardWorld {

    public Map<UUID, MainCardGame> tableInWorld = new HashMap<>();

    public MainCardGame getGame(UUID player) {
        return tableInWorld.get(player);
    }

    public void addMainCardGame(UUID uuid, TableEntity tableEntity, Entity enemy) {
        tableInWorld.put(uuid, new MainCardGame(tableEntity.getUuid(), enemy.getUuid(), uuid));
    }

    public void removeMainCardGame(UUID uuid) {
        tableInWorld.remove(uuid);
    }



    public void interaction(int i, UUID uuid, MinecraftServer server) {

        MainCardGame game = tableInWorld.get(uuid);
        if(game == null) return;

        InteractionType[] types = InteractionType.values();
        if (i < 0 || i >= types.length) return;

        switch (types[i]) {
            case TAKE_CARD -> game.takeCard(server);
            case TAKE_ANIMAL -> game.takeAnimal(server);
            case PLAY_ATTACK -> game.startBattle(server);
        }
    }

    public void tick(MinecraftServer server) {
        for(MainCardGame mainCardGame : tableInWorld.values()) {
            mainCardGame.tick(server);
        }
    }
}
