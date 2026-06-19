package net.noist.highlight_api.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class PrimitiveRenderer {
    public static void renderFilledBox(PoseStack poseStack, VertexConsumer buffer, AABB aabb, float r, float g, float b, float a) {
        Matrix4f matrix = poseStack.last().pose();

        float x1 = (float) aabb.minX;
        float y1 = (float) aabb.minY;
        float z1 = (float) aabb.minZ;
        float x2 = (float) aabb.maxX;
        float y2 = (float) aabb.maxY;
        float z2 = (float) aabb.maxZ;

        // Bottom
        RendererUtils.drawQuad(matrix, buffer, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, r, g, b, a);
        // Top
        RendererUtils.drawQuad(matrix, buffer, x1, y2, z1, x1, y2, z2, x2, y2, z2, x2, y2, z1, r, g, b, a);
        // North
        RendererUtils.drawQuad(matrix, buffer, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, r, g, b, a);
        // South
        RendererUtils.drawQuad(matrix, buffer, x1, y1, z2, x2, y1, z2, x2, y2, z2, x1, y2, z2, r, g, b, a);
        // West
        RendererUtils.drawQuad(matrix, buffer, x1, y1, z1, x1, y1, z2, x1, y2, z2, x1, y2, z1, r, g, b, a);
        // East
        RendererUtils.drawQuad(matrix, buffer, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2, r, g, b, a);
    }

    public static void renderOutlineBoxAsQuads(PoseStack poseStack, VertexConsumer buffer, AABB aabb, float r, float g, float b, float a, float lineWidth) {
        Matrix4f matrix = poseStack.last().pose();
        float hw = lineWidth / 2.0f;

        float x1 = (float) aabb.minX;
        float y1 = (float) aabb.minY;
        float z1 = (float) aabb.minZ;
        float x2 = (float) aabb.maxX;
        float y2 = (float) aabb.maxY;
        float z2 = (float) aabb.maxZ;

        RendererUtils.drawLineX(matrix, buffer, x1, x2, y1, z1, hw, r, g, b, a);
        RendererUtils.drawLineX(matrix, buffer, x1, x2, y1, z2, hw, r, g, b, a);
        RendererUtils.drawLineX(matrix, buffer, x1, x2, y2, z1, hw, r, g, b, a);
        RendererUtils.drawLineX(matrix, buffer, x1, x2, y2, z2, hw, r, g, b, a);

        RendererUtils.drawLineY(matrix, buffer, y1, y2, x1, z1, hw, r, g, b, a);
        RendererUtils.drawLineY(matrix, buffer, y1, y2, x2, z1, hw, r, g, b, a);
        RendererUtils.drawLineY(matrix, buffer, y1, y2, x1, z2, hw, r, g, b, a);
        RendererUtils.drawLineY(matrix, buffer, y1, y2, x2, z2, hw, r, g, b, a);

        RendererUtils.drawLineZ(matrix, buffer, z1, z2, x1, y1, hw, r, g, b, a);
        RendererUtils.drawLineZ(matrix, buffer, z1, z2, x2, y1, hw, r, g, b, a);
        RendererUtils.drawLineZ(matrix, buffer, z1, z2, x1, y2, hw, r, g, b, a);
        RendererUtils.drawLineZ(matrix, buffer, z1, z2, x2, y2, hw, r, g, b, a);
    }
}