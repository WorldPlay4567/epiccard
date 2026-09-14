package com.epiccard.client;

import com.epiccard.EpicCard;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.util.Identifier;

public class ModelsRegister {

    public static final Identifier TABLE_MODEL = EpicCard.id("table");
    public static final Identifier BATTLE_PAINT_MODEL = EpicCard.id("battle_paint");
    public static final Identifier CARD_MODEL = EpicCard.id("card");

    public static void registerModels() {
        ModelLoadingPlugin.register((context -> {
            context.addModels(TABLE_MODEL);
            context.addModels(BATTLE_PAINT_MODEL);
            context.addModels(CARD_MODEL);
        }));
    }
}
