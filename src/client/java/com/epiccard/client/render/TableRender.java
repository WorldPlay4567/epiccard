package com.epiccard.client.render;

import com.epiccard.entity.entity.TableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class TableRender {

    TableEntity tableEntity;

    public TableRender(TableEntity tableEntity) {
        this.tableEntity = tableEntity;
    }

    public Vec3d getVec3d() {
        return tableEntity.getPos();
    }

    public TableEntity getTableEntity() {
        return tableEntity;
    }
}
