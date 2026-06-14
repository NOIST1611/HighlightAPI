package net.noist.highlight_api.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.noist.highlight_api.HighlightAPI;
import net.noist.highlight_api.HighlightHandle;
import net.noist.highlight_api.HighlightManager;
import net.noist.highlight_api.animation.ICustomAnimation;
import net.noist.highlight_api.enums.AnimationType;
import net.noist.highlight_api.enums.DepthMode;
import net.noist.highlight_api.enums.Lifetime;
import net.noist.highlight_api.enums.RenderMode;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HighlightRenderer {

    private static final RenderType FILL_RENDER_TYPE = RenderType.create(
            "highlight_fill",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256, false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader))
                    .setTransparencyState(new RenderStateShard.TransparencyStateShard("translucent_transparency",
                            () -> {
                                RenderSystem.enableBlend();
                                RenderSystem.blendFuncSeparate(
                                        GlStateManager.SourceFactor.SRC_ALPHA,
                                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                                        GlStateManager.SourceFactor.ONE,
                                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
                                );
                            },
                            () -> {
                                RenderSystem.disableBlend();
                                RenderSystem.defaultBlendFunc();
                            }
                    ))
                    .setDepthTestState(new RenderStateShard.DepthTestStateShard("always", 519))
                    .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                    .createCompositeState(false)
    );

    private static void setupRender(HighlightHandle handle) {
        if (handle.getDepthMode() == DepthMode.IGNORE) {
            RenderSystem.disableDepthTest();
        }
        RenderSystem.lineWidth(handle.getLineWidth());
    }

    private static void teardownRender(HighlightHandle handle) {
        RenderSystem.lineWidth(1.0f);
        if (handle.getDepthMode() == DepthMode.IGNORE) {
            RenderSystem.enableDepthTest();
        }
    }

    private static float getAnimationAlpha(HighlightHandle handle, float partialTick) {
        ICustomAnimation anim = handle.getCustomAnimation();
        if (anim == null) return 1.0f;
        return anim.tick(partialTick) * handle.getAnimationSpeed();
    }

    private static void renderFill(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, AABB aabb, float[] color, float alpha) {
        VertexConsumer buffer = bufferSource.getBuffer(FILL_RENDER_TYPE);
        Matrix4f matrix = poseStack.last().pose();

        float r = color[0], g = color[1], b = color[2], a = color[3] * alpha;
        float x1 = (float)aabb.minX, y1 = (float)aabb.minY, z1 = (float)aabb.minZ;
        float x2 = (float)aabb.maxX, y2 = (float)aabb.maxY, z2 = (float)aabb.maxZ;

        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a).endVertex();

        bufferSource.endBatch(FILL_RENDER_TYPE);
    }

    private static void renderBlock(RenderLevelStageEvent event, HighlightHandle handle, BlockPos pos) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
        float partialTick = event.getPartialTick();
        float alpha = getAnimationAlpha(handle, partialTick);

        setupRender(handle);
        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);

        renderFill(poseStack, bufferSource, new AABB(pos).inflate(0.002), handle.getFillColor(), alpha);

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.lines());
        float[] color = handle.getOutlineColor();
        LevelRenderer.renderLineBox(poseStack, buffer, new AABB(pos).inflate(0.002), color[0], color[1], color[2], color[3] * alpha);

        poseStack.popPose();
        bufferSource.endBatch(RenderType.lines());
        teardownRender(handle);
    }

    private static void renderEntity(RenderLevelStageEvent event, HighlightHandle handle, Entity entity) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
        float partialTick = event.getPartialTick();
        float alpha = getAnimationAlpha(handle, partialTick);

        setupRender(handle);
        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);

        renderFill(poseStack, bufferSource, entity.getBoundingBox().inflate(0.002), handle.getFillColor(), alpha);

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.lines());
        float[] color = handle.getOutlineColor();
        LevelRenderer.renderLineBox(poseStack, buffer, entity.getBoundingBox().inflate(0.002), color[0], color[1], color[2], color[3] * alpha);

        poseStack.popPose();
        bufferSource.endBatch(RenderType.lines());
        teardownRender(handle);
    }

    private static void renderAABB(RenderLevelStageEvent event, HighlightHandle handle, AABB aabb) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
        float partialTick = event.getPartialTick();
        float alpha = getAnimationAlpha(handle, partialTick);

        setupRender(handle);
        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);

        renderFill(poseStack, bufferSource, aabb.inflate(0.002), handle.getFillColor(), alpha);

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.lines());
        float[] color = handle.getOutlineColor();
        LevelRenderer.renderLineBox(poseStack, buffer, aabb.inflate(0.002), color[0], color[1], color[2], color[3] * alpha);

        poseStack.popPose();
        bufferSource.endBatch(RenderType.lines());
        teardownRender(handle);
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();

        for (HighlightHandle handle : HighlightManager.getAllHandlers()) {
            if (!handle.isVisible() || handle.isRemoved()) continue;

            Object target = handle.getTarget();

            if (handle.getRenderMode() == RenderMode.REGION) {
                if (target instanceof BlockPos start && handle.getTargetEnd() instanceof BlockPos end) {
                    AABB regionAABB = new AABB(start).minmax(new AABB(end));
                    renderAABB(event, handle, regionAABB);
                }
                continue;
            }

            if (target instanceof BlockPos blockPos) {
                renderBlock(event, handle, blockPos);
            } else if (target instanceof Entity entity) {
                renderEntity(event, handle, entity);
            } else if (target instanceof AABB aabb) {
                renderAABB(event, handle, aabb);
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.ClientTickEvent.Phase.END) return;
        HighlightManager.tick(0.05f);
    }
}