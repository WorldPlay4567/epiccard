package com.epiccard.entity.entity;

import com.epiccard.game.InteractionType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class InteractionCardEntity extends Entity {


    public InteractionType interactionType;
    public int id;
    public InteractionCardEntity(EntityType<?> type, World world) {
        super(type, world);
    }



    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
