package com.epiccard.client.game;

import com.epiccard.entity.EntityRegister;
import com.epiccard.entity.entity.InteractionCardEntity;
import com.epiccard.entity.entity.TableEntity;
import com.epiccard.game.InteractionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class TableManager {

    private static final List<TableEntity> tableEntityList = new ArrayList<>();
    private static int idCardEntity = 0;
    private static final List<Table> tableList = new ArrayList<>();

    public static void addTableEntity(TableEntity tableEntity) {
        tableEntityList.add(tableEntity);
        spawnInteraction(tableEntity);
    }

    public static void removeTableEntity(TableEntity tableEntity) {
        tableEntityList.remove(tableEntity);
        removeInteraction(tableEntity);
    }

    public static void spawnInteraction(TableEntity tableEntity) {
        tableList.add(new Table(tableEntity).create());
    }

    private static void removeInteraction(TableEntity tableEntity) {
        for(Table table : tableList) {
            if(table.tableEntity == tableEntity) {
                table.remove();
            }
        }
        tableList.removeIf((table -> table.tableEntity == tableEntity));
    }


    public static class Table {
        List<InteractionCardEntity> interactionCardEntityList = new ArrayList<>();
        TableEntity tableEntity;
        public Table(TableEntity tableEntity) {
            this.tableEntity = tableEntity;
        }

        public Table create() {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientWorld world = client.world;

            {
                InteractionCardEntity interactionCardEntity = new InteractionCardEntity(EntityRegister.INTERACTION_CARD_ENTITY_ENTITY_TYPE, world);
                interactionCardEntity.interactionType = InteractionType.PLAY_ATTACK;
                interactionCardEntity.setPosition(tableEntity.getPos().add(0.4,1,1.0));
                interactionCardEntity.setId(Integer.MAX_VALUE - idCardEntity);
                --idCardEntity;
                world.addEntity(interactionCardEntity);
                interactionCardEntityList.add(interactionCardEntity);
            }

            {
                InteractionCardEntity interactionCardEntity = new InteractionCardEntity(EntityRegister.INTERACTION_CARD_ENTITY_ENTITY_TYPE, world);
                interactionCardEntity.interactionType = InteractionType.TAKE_ANIMAL;
                interactionCardEntity.setPosition(tableEntity.getPos().add(0.4,1,-1.1));
                interactionCardEntity.setId(Integer.MAX_VALUE - idCardEntity);
                --idCardEntity;
                world.addEntity(interactionCardEntity);
                interactionCardEntityList.add(interactionCardEntity);
            }

            {
                InteractionCardEntity interactionCardEntity = new InteractionCardEntity(EntityRegister.INTERACTION_CARD_ENTITY_ENTITY_TYPE, world);
                interactionCardEntity.interactionType = InteractionType.TAKE_CARD;
                interactionCardEntity.setPosition(tableEntity.getPos().add(0.4,1,-0.8));
                interactionCardEntity.setId(Integer.MAX_VALUE - idCardEntity);
                --idCardEntity;
                world.addEntity(interactionCardEntity);
                interactionCardEntityList.add(interactionCardEntity);
            }

            for(int x = 0; x < 4; x++) {
                    InteractionCardEntity interactionCardEntity = new InteractionCardEntity(EntityRegister.INTERACTION_CARD_ENTITY_ENTITY_TYPE, world);
                    interactionCardEntity.setPosition(tableEntity.getPos().add(0.4,1, -0.54 + (0.36 * x)));
                    interactionCardEntity.id = x;
                    interactionCardEntity.interactionType = InteractionType.PLACE_CARD;
                    interactionCardEntity.setId(Integer.MAX_VALUE - idCardEntity);
                    --idCardEntity;
                    world.addEntity(interactionCardEntity);
                    interactionCardEntityList.add(interactionCardEntity);

            }
            return this;
        }

        public void remove() {
            for(InteractionCardEntity interactionCardEntity : interactionCardEntityList) {
                interactionCardEntity.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);
            }
            interactionCardEntityList.clear();
        }
    }
}
