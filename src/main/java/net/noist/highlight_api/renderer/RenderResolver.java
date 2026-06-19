package net.noist.highlight_api.renderer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.noist.highlight_api.HighlightHandle;
import net.noist.highlight_api.enums.RenderMode;

public final class RenderResolver {
    private RenderResolver() {}

    public static AABB resolve(HighlightHandle handle, float partialTick) {
        Object target = handle.getTarget();

        if (target == null) {
            return new AABB(0, 0, 0, 0, 0, 0);
        }

        if (handle.getRenderMode() == RenderMode.REGION) {
            if (target instanceof BlockPos start && handle.getTargetEnd() instanceof BlockPos end) {
                return new AABB(start).minmax(new AABB(end)).inflate(0.002);
            }
            return new AABB(0, 0, 0, 0, 0, 0); // Если точки нет, пустой AABB
        }

        if (target instanceof Entity entity) {
            double x = entity.xOld + (entity.getX() - entity.xOld) * partialTick;
            double y = entity.yOld + (entity.getY() - entity.yOld) * partialTick;
            double z = entity.zOld + (entity.getZ() - entity.zOld) * partialTick;

            return entity.getBoundingBox().move(
                    x - entity.getX(),
                    y - entity.getY(),
                    z - entity.getZ()
            ).inflate(0.002);
        } else if (target instanceof BlockPos blockPos) {
            return new AABB(blockPos).inflate(0.002);
        } else if (target instanceof AABB aabb) {
            return aabb.inflate(0.002);
        }

        return new AABB(0, 0, 0, 0, 0, 0);
    }
}