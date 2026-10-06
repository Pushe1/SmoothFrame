package com.smoothframe.perf;

/**
 * Chunk render telemetry placeholder for Minecraft 26.2.
 *
 * NeoForge 26.2 changed the internal chunk rendering dispatcher API,
 * so SmoothFrame does not access those internals until a stable public API
 * is available.
 */
public final class ChunkRenderMonitor {

    public void tick() {
        // Intentionally empty.
        // Chunk rendering is handled by vanilla/NeoForge.
    }

    public int queueSize() {
        return 0;
    }

    public int freeBuffers() {
        return 0;
    }

    public String stats() {
        return "unavailable";
    }
}
