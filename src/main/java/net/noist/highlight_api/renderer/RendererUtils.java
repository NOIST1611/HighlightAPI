package net.noist.highlight_api.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

public final class RendererUtils {
    private RendererUtils() {
    }

    public static void drawQuad(Matrix4f matrix, VertexConsumer buffer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float r, float g, float b, float a) {
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x3, y3, z3).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x4, y4, z4).color(r, g, b, a).endVertex();
    }

    public static void drawLineX(Matrix4f matrix, VertexConsumer buffer, float x1, float x2, float y, float z, float hw, float r, float g, float b, float a) {
        float startX = x1 - hw;
        float endX = x2 + hw;

        drawQuad(matrix, buffer,
                startX, y - hw, z,
                endX, y - hw, z,
                endX, y + hw, z,
                startX, y + hw, z,
                r, g, b, a);

        drawQuad(matrix, buffer,
                startX, y, z - hw,
                endX, y, z - hw,
                endX, y, z + hw,
                startX, y, z + hw,
                r, g, b, a);
    }

    public static void drawLineY(Matrix4f matrix, VertexConsumer buffer, float y1, float y2, float x, float z, float hw, float r, float g, float b, float a) {
        float startY = y1 - hw;
        float endY = y2 + hw;

        drawQuad(matrix, buffer,
                x - hw, startY, z,
                x + hw, startY, z,
                x + hw, endY, z,
                x - hw, endY, z,
                r, g, b, a);

        drawQuad(matrix, buffer,
                x, startY, z - hw,
                x, startY, z + hw,
                x, endY, z + hw,
                x, endY, z - hw,
                r, g, b, a);
    }

    public static void drawLineZ(Matrix4f matrix, VertexConsumer buffer, float z1, float z2, float x, float y, float hw, float r, float g, float b, float a) {
        float startZ = z1 - hw;
        float endZ = z2 + hw;

        drawQuad(matrix, buffer,
                x - hw, y, startZ,
                x + hw, y, startZ,
                x + hw, y, endZ,
                x - hw, y, endZ,
                r, g, b, a);

        drawQuad(matrix, buffer,
                x, y - hw, startZ,
                x, y + hw, startZ,
                x, y + hw, endZ,
                x, y - hw, endZ,
                r, g, b, a);
    }
}