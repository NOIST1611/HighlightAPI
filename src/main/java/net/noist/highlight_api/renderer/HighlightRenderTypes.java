package net.noist.highlight_api.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.noist.highlight_api.enums.DepthMode;

import java.util.HashMap;

public final class HighlightRenderTypes {
    private HighlightRenderTypes() {}

    private static HashMap<String, RenderType> renderTypes = new HashMap<>();

    private static void createType(String name, boolean ignoreDepth) {
        RenderType.CompositeState.CompositeStateBuilder builder = RenderType.CompositeState.builder()
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
                .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                .setCullState(new RenderStateShard.CullStateShard(false));

        if (ignoreDepth) {
            builder.setDepthTestState(new RenderStateShard.DepthTestStateShard("always", 519));
        }

        renderTypes.put(name, RenderType.create(
                name,
                DefaultVertexFormat.POSITION_COLOR,
                VertexFormat.Mode.QUADS,
                256, false, true,
                builder.createCompositeState(false)
        ));
    }

    public static RenderType getRenderType(DepthMode mode) {
        if (mode == DepthMode.IGNORE) {
            return renderTypes.get("highlight_ignore");
        }
        return renderTypes.get("highlight_hide");
    }

    static {
        createType("highlight_ignore", true);
        createType("highlight_hide", false);
    }
}