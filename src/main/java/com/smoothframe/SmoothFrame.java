package com.smoothframe;

import com.smoothframe.config.SmoothFrameConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(SmoothFrame.MOD_ID)
public final class SmoothFrame {
    public static final String MOD_ID = "smoothframe";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public SmoothFrame(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, SmoothFrameConfig.CLIENT_SPEC);
        LOGGER.info("SmoothFrame {} initialized", SmoothFrameConfig.VERSION);
    }
}
