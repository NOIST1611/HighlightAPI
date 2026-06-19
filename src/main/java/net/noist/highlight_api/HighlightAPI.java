package net.noist.highlight_api;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.noist.highlight_api.enums.Lifetime;
import org.slf4j.Logger;

@Mod(HighlightAPI.MODID)
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HighlightAPI {
    public static final String MODID = "highlight_api";
    public static final Logger LOGGER = LogUtils.getLogger();

    public HighlightAPI(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Main entry point for the Highlight API.
     * Use this class to create and register highlights in the world.
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * HighlightHandle handle = HighlightAPI.create(Lifetime.ETERNAL)
     *     .setTarget(blockPos)
     *     .setOutlineColor(1.0f, 0.0f, 0.0f, 1.0f)
     *     .setFillColor(1.0f, 0.0f, 0.0f, 0.3f)
     *     .register();
     * }</pre>
     */
    public static HighlightBuilder create(Lifetime lifetime) {
        return new HighlightBuilder(lifetime);
    }

    /**
     * Creates a new highlight builder with a delayed lifetime.
     *
     * @param lifetime use {@link Lifetime#DELAYED}
     * @param delay time in seconds before the highlight is removed
     * @return a new {@link HighlightBuilder} instance
     */
    public static HighlightBuilder create(Lifetime lifetime, float delay) {
        return new HighlightBuilder(lifetime, delay);
    }
}