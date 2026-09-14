package com.epiccard.client.render;

import com.epiccard.client.ModelsRegister;
import com.epiccard.entity.entity.TableEntity;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableManagerRender {
    public static List<TableRender> tableEntityList = new ArrayList<>();

    public static void init() {

        WorldRenderEvents.AFTER_ENTITIES.register((TableManagerRender::render));

    }

    public static void removeTable(TableEntity tableEntity) {
        tableEntityList.removeIf(tableRender -> tableRender.tableEntity == tableEntity);
    }

    public static void addTable(TableRender tableRender) {
        tableEntityList.add(tableRender);
    }

    private static void render(WorldRenderContext context) {

        if(context.matrixStack() == null) {
            return;
        }

        MatrixStack matrices = context.matrixStack();
        VertexConsumerProvider vertexConsumer = context.consumers();
        Vec3d camPos = context.camera().getPos();

        for(TableRender tableRender : tableEntityList) {
            TableEntity tableEntity = tableRender.getTableEntity();
            Vec3d pos = tableEntity.getPos().subtract(camPos);

            matrices.push();

            matrices.translate(pos.getX() - 0.2,pos.getY() + 1.345,pos.getZ());
            matrices.scale(0.7f, 0.7f, 0.7f);
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(90));
            BakedModel _model =  MinecraftClient.getInstance().getBakedModelManager().getModel(ModelsRegister.BATTLE_PAINT_MODEL);

            MinecraftClient.getInstance().getItemRenderer().renderItem(
                    Items.STONE.getDefaultStack(),
                    ModelTransformationMode.FIXED,
                    false,
                    matrices,
                    vertexConsumer,
                    LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE,
                    OverlayTexture.DEFAULT_UV,
                    _model
            );

            matrices.pop();

            matrices.push();
            matrices.translate(pos.getX(),pos.getY() + 0.5,pos.getZ());
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(90));
            BakedModel model =  MinecraftClient.getInstance().getBakedModelManager().getModel(ModelsRegister.TABLE_MODEL);

            MinecraftClient.getInstance().getItemRenderer().renderItem(
                    Items.STONE.getDefaultStack(),
                    ModelTransformationMode.FIXED,
                    false,
                    matrices,
                    vertexConsumer,
                    LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE,
                    OverlayTexture.DEFAULT_UV,
                    model
            );

            matrices.pop();
        }

    }

}
