package com.smoothframe.render;

import com.smoothframe.config.SmoothFrameConfig;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

/**
 * Cheap distance culling at the final living-entity render hook.
 * This only suppresses visual submission; entities continue to tick normally.
 */
public final class LivingEntityCulling {
    private LivingEntityCulling() {}

    public static void onPre(RenderLivingEvent.Pre<?, ? extends LivingEntityRenderState, ?> event) {
        if (!SmoothFrameConfig.LIVING_ENTITY_CULLING.get()) return;

        LivingEntityRenderState state = event.getRenderState();
        double max = SmoothFrameConfig.LIVING_ENTITY_DISTANCE.get();
        if (state.distanceToCameraSq > max * max) {
            event.setCanceled(true);
        }
    }
}
