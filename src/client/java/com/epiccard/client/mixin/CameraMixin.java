package com.epiccard.client.mixin;


import com.epiccard.client.screen.BattleScreen;
import com.epiccard.entity.entity.TableEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    protected abstract void setPos(double x, double y, double z);

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "update", at = @At(value = "TAIL"))
    private void update$mixin(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {

        if(MinecraftClient.getInstance().currentScreen instanceof BattleScreen battleScreen) {
            TableEntity tableEntity = battleScreen.getTableEntity();
            setPos(tableEntity.getX() + 1, tableEntity.getY() + 1.4, tableEntity.getZ());
            setRotation(90,20);
        }
    }
}
