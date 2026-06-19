package net.noist.highlight_api.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.noist.highlight_api.HighlightAPI;
import net.noist.highlight_api.HighlightHandle;
import net.noist.highlight_api.HighlightManager;
import net.noist.highlight_api.enums.AnimationScope;
import net.noist.highlight_api.enums.AnimationType;
import net.noist.highlight_api.enums.DepthMode;
import net.noist.highlight_api.enums.Lifetime;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Mod.EventBusSubscriber
public class HighlightRenderer {
    public static void renderOverClouds(PoseStack poseStack, float partialTick, Camera camera) {
        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        Vec3 camPos = camera.getPosition();

        Matrix4f projection = RenderSystem.getProjectionMatrix();
        double tanHalfFov = 1.0 / projection.m11();
        int screenHeight = mc.getWindow().getHeight();

        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);

        for (HighlightHandle handle : HighlightManager.getAllHandlers()) {
            if (!handle.isVisible() || handle.isRemoved()) continue;

            AABB aabb = RenderResolver.resolve(handle, partialTick);
            if (aabb.getXsize() <= 0.0 && aabb.getYsize() <= 0.0 && aabb.getZsize() <= 0.0) continue;

            Vector3f lookVec = camera.getLookVector();
            double centerX = aabb.minX + (aabb.maxX - aabb.minX) * 0.5;
            double centerY = aabb.minY + (aabb.maxY - aabb.minY) * 0.5;
            double centerZ = aabb.minZ + (aabb.maxZ - aabb.minZ) * 0.5;
            double depth = (centerX - camPos.x) * lookVec.x +
                    (centerY - camPos.y) * lookVec.y +
                    (centerZ - camPos.z) * lookVec.z;
            if (depth < 0.1) depth = 0.1;

            double worldPerPixel = (2.0 * tanHalfFov * depth) / screenHeight;
            double blockSizeInPixels = 1.0 / worldPerPixel;
            float desiredPixels = handle.getLineWidth();

            float maxPixels = (float) (blockSizeInPixels * 0.05);

            float actualPixels = Math.min(desiredPixels, maxPixels);

            float lineWidthInBlocks = (float) (actualPixels * worldPerPixel);
            float lineWidth = Math.max(0.001f, lineWidthInBlocks);

            float[] outlineColor = handle.getOutlineColor();
            float[] fillColor = handle.getFillColor();

            float oR = outlineColor[0], oG = outlineColor[1], oB = outlineColor[2], oA = outlineColor[3];
            float fR = fillColor[0], fG = fillColor[1], fB = fillColor[2], fA = fillColor[3];

            float animValue = 1.0f;
            if (handle.getCustomAnimation() != null) {
                animValue = handle.getCustomAnimation().tick(partialTick);
            }

            if (handle.getAnimationScope() == AnimationScope.ALPHA) {
                oA *= animValue;
                fA *= animValue;
            } else if (handle.getAnimationScope() == AnimationScope.COLOR) {
                oR *= animValue; oG *= animValue; oB *= animValue;
                fR *= animValue; fG *= animValue; fB *= animValue;
            }

            VertexConsumer buffer = bufferSource.getBuffer(HighlightRenderTypes.getRenderType(handle.getDepthMode()));

            PrimitiveRenderer.renderOutlineBoxAsQuads(poseStack, buffer, aabb, oR, oG, oB, oA, lineWidth);
            PrimitiveRenderer.renderFilledBox(poseStack, buffer, aabb, fR, fG, fB, fA);
        }

        poseStack.popPose();
        bufferSource.endBatch(HighlightRenderTypes.getRenderType(DepthMode.IGNORE));
        bufferSource.endBatch(HighlightRenderTypes.getRenderType(DepthMode.HIDE));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.ClientTickEvent.Phase.END) return;
        HighlightManager.tick(0.05f);
    }
}