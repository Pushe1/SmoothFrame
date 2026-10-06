package com.smoothframe.render;

import com.smoothframe.config.SmoothFrameConfig;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;

/**
 * Cheap name-tag culling that runs after Minecraft has extracted the render state.
 * It avoids touching world/entity collections and therefore stays on the render thread.
 */
public final class NameTagCulling {
    private NameTagCulling() {}

    public static void onCanRender(RenderNameTagEvent.CanRender event) {
        if (!SmoothFrameConfig.NAME_TAG_CULLING.get()) return;

        EntityRenderState state = event.getEntityRenderState();
        double maxDistance = SmoothFrameConfig.NAME_TAG_DISTANCE.get();
        if (state.distanceToCameraSq > maxDistance * maxDistance) {
            event.setCanRender(TriState.FALSE);
        }
    }
}
