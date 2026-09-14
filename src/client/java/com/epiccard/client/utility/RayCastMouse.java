package com.epiccard.client.utility;


import com.epiccard.client.mixin.GameRendererInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.function.Predicate;

public class RayCastMouse {


    /**
     * Выполняет рейкаст от позиции мыши на экране в 3D-мир.
     * Возвращает EntityHitResult, если под мышкой есть моб (в радиусе 100 блоков),
     * или null, если моба нет или он скрыт за стеной.
     */
    public static EntityHitResult getMobUnderMouse(double mouseX, double mouseY) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return null;

        Camera camera = client.gameRenderer.getCamera();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        // 1. ИСПРАВЛЕНИЕ: Берем реальный (динамический) FOV, а не значение из настроек!
        float tickDelta = client.getRenderTickCounter().getTickDelta(true); // Для 1.21.1
        double fov = ((GameRendererInvoker) client.gameRenderer).invokeGetFov(camera, tickDelta, true);

        // 2. Математика: перевод 2D координат экрана в 3D вектор направления
        Matrix4f projMatrix = client.gameRenderer.getBasicProjectionMatrix(fov);
        Matrix4f viewMatrix = new Matrix4f().rotation(camera.getRotation().conjugate());
        Matrix4f invCombined = new Matrix4f(projMatrix).mul(viewMatrix).invert();

        // Нормализованные координаты устройства (NDC)
        float nX = (float) ((2.0 * mouseX) / width - 1.0);
        float nY = (float) (1.0 - (2.0 * mouseY) / height);

        // ИСПРАВЛЕНИЕ: ближняя плоскость в OpenGL начинается с -1.0f, а не с 0.0f
        Vector3f nearPos = new Vector3f(nX, nY, -1.0f).mulProject(invCombined);
        Vector3f farPos = new Vector3f(nX, nY, 1.0f).mulProject(invCombined);

        Vec3d start = camera.getPos();
        Vec3d direction = new Vec3d(farPos.x(), farPos.y(), farPos.z())
                .subtract(nearPos.x(), nearPos.y(), nearPos.z())
                .normalize();

        // 3. Устанавливаем длину луча в 100 блоков
        double maxDistance = 100.0;
        Vec3d end = start.add(direction.multiply(maxDistance));

        // 4. Сначала пускаем луч в блоки, чтобы не "простреливать" стены
        BlockHitResult blockHit = client.world.raycast(new RaycastContext(
                start, end,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                client.player
        ));

        // Если луч уперся в блок, ограничиваем дистанцию
        double currentMaxDistance = maxDistance;
        if (blockHit.getType() != HitResult.Type.MISS) {
            currentMaxDistance = blockHit.getPos().distanceTo(start);
            end = start.add(direction.multiply(currentMaxDistance));
        }

        // 5. Поиск мобов (LivingEntity)
        return findMobRaycast(client.player, start, end, currentMaxDistance);
    }
    private static EntityHitResult findMobRaycast(Entity shooter, Vec3d start, Vec3d end, double maxDistance) {
        // Создаем коробку для поиска кандидатов.
        // Так как дистанция 100 блоков, коробка будет длинной.
        Box searchBox = shooter.getBoundingBox()
                .stretch(end.subtract(start))
                .expand(5.0D, 5.0D, 5.0D);

        // Фильтр: ищем только живых сущностей (мобы/игроки), исключаем себя и наблюдателей
        Predicate<Entity> filter = entity ->
                !entity.isSpectator() && // Ограничиваем поиск только мобами
                        entity != shooter;                // На всякий случай дублируем проверку на себя

        return ProjectileUtil.raycast(
                shooter,
                start,
                end,
                searchBox,
                filter,
                maxDistance * maxDistance // Требуется квадрат дистанции
        );
    }
}