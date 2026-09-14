package com.epiccard.entity;

import com.epiccard.EpicCard;
import com.epiccard.entity.entity.InteractionCardEntity;
import com.epiccard.entity.entity.TableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegister {

    public static final EntityType<TableEntity> TABLE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            EpicCard.id("table_entity"),
            EntityType.Builder.create(TableEntity::new, SpawnGroup.CREATURE).dimensions(1,1).build()
            );

    public static final EntityType<InteractionCardEntity> INTERACTION_CARD_ENTITY_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            EpicCard.id("interaction_card_entity"),
            EntityType.Builder.create(InteractionCardEntity::new, SpawnGroup.CREATURE).dimensions(0.2f,0.2f).build()
    );
    public static void init() {

    }



}
