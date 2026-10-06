package com.smoothframe.perf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;

/**
 * Reads the real NeoForge 26.2 terrain compilation queue without replacing it,
 * creating a second executor, or touching Minecraft world state off-thread.
 */
public final class ChunkRenderMonitor {
    private int queueSize;
    private int freeBuffers;
    private String stats = "unavailable";
    private int frameCounter;

    public void tick() {
        if (!com.smoothframe.config.SmoothFrameConfig.CHUNK_TELEMETRY.get()) return;
        if (++frameCounter < com.smoothframe.config.SmoothFrameConfig.CHUNK_SAMPLE_INTERVAL.get()) return;
        frameCounter = 0;

        Minecraft mc = Minecraft.getInstance();
        LevelRenderer renderer = mc.levelRenderer;
        if (renderer == null) {
            queueSize = 0;
            freeBuffers = 0;
            stats = "unavailable";
            return;
        }

        SectionRenderDispatcher dispatcher = renderer.getSectionRenderDispatcher();
        if (dispatcher == null) {
            queueSize = 0;
            freeBuffers = 0;
            stats = "unavailable";
            return;
        }

        queueSize = dispatcher.getCompileQueueSize();
        freeBuffers = dispatcher.getFreeBufferCount();
        stats = dispatcher.getStats();
    }

    public int queueSize() { return queueSize; }
    public int freeBuffers() { return freeBuffers; }
    public String stats() { return stats; }
}
