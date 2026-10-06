package com.smoothframe;

import com.smoothframe.config.SmoothFrameConfig;
import com.smoothframe.debug.FrameTimeTracker;
import com.smoothframe.debug.PerformanceHud;
import com.smoothframe.perf.GcMonitor;
import com.smoothframe.perf.ChunkRenderMonitor;
import com.smoothframe.render.NameTagCulling;
import com.smoothframe.render.LivingEntityCulling;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = SmoothFrame.MOD_ID, dist = Dist.CLIENT)
public final class SmoothFrameClient {
    private final FrameTimeTracker tracker = new FrameTimeTracker();
    private final PerformanceHud hud = new PerformanceHud();
    private final GcMonitor gcMonitor = new GcMonitor();
    private final ChunkRenderMonitor chunkMonitor = new ChunkRenderMonitor();
    private int gcSampleCounter;
    private long lastLoggedWorstNanos;

    public SmoothFrameClient(IEventBus modBus, ModContainer container) {
        NeoForge.EVENT_BUS.addListener(this::onFramePre);
        NeoForge.EVENT_BUS.addListener(this::onFramePost);
        NeoForge.EVENT_BUS.addListener(this::onGuiPost);
        NeoForge.EVENT_BUS.addListener(NameTagCulling::onCanRender);
        NeoForge.EVENT_BUS.addListener(LivingEntityCulling::onPre);
        SmoothFrame.LOGGER.info("SmoothFrame client layer initialized");
    }

    private void onFramePre(RenderFrameEvent.Pre event) {
        if (!SmoothFrameConfig.PROFILER.get()) return;
        tracker.beginFrame(System.nanoTime());
    }

    private void onFramePost(RenderFrameEvent.Post event) {
        if (!SmoothFrameConfig.PROFILER.get()) return;

        long now = System.nanoTime();
        long threshold = SmoothFrameConfig.SPIKE_THRESHOLD_MS.get().longValue() * 1_000_000L;
        tracker.endFrame(now, threshold);

        // GC MXBean queries are deliberately sampled periodically instead of every frame.
        if (++gcSampleCounter >= 30) {
            gcSampleCounter = 0;
            gcMonitor.sample();
            chunkMonitor.tick();
        }

        if (SmoothFrameConfig.LOG_SPIKES.get()) {
            long worstNanos = (long) (tracker.worstFrameMs() * 1_000_000.0);
            if (worstNanos > lastLoggedWorstNanos && worstNanos >= threshold) {
                lastLoggedWorstNanos = worstNanos;
                SmoothFrame.LOGGER.warn("Frame-time spike: worst frame {:.2f} ms (threshold {} ms)",
                        tracker.worstFrameMs(), SmoothFrameConfig.SPIKE_THRESHOLD_MS.get());
            }
        }
    }

    private void onGuiPost(RenderGuiEvent.Post event) {
        if (SmoothFrameConfig.PROFILER.get() && SmoothFrameConfig.HUD.get()) {
            hud.render(event.getGuiGraphics(), tracker, gcMonitor);
        }
    }
}
