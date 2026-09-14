package com.epiccard.client.mixin;


import com.epiccard.entity.entity.TableEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(GameRenderer.class)
public class RayCastMixin {
    @ModifyArg(method = "findCrosshairTarget", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"),
            index = 4)
    private Predicate<Entity> raycast$mixin(Predicate<Entity> predicate) {

        return (entity) -> !entity.isSpectator() && entity.canHit() && !(entity instanceof TableEntity);
    }

}
