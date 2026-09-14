package com.epiccard.entity.entity;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class TableEntity extends Entity {
    public TableEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canHit() {
        return true;
    }

    public void interaction() {
        if(this.getWorld().isClient()) {

        }
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
