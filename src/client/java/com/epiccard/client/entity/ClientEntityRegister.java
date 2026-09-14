package com.epiccard.client.entity;

import com.epiccard.entity.EntityRegister;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EmptyEntityRenderer;

public class ClientEntityRegister {

    public static void init() {
        EntityRendererRegistry.register(EntityRegister.TABLE_ENTITY, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegister.INTERACTION_CARD_ENTITY_ENTITY_TYPE, EmptyEntityRenderer::new);
    }
}
