package com.smoothframe.debug;

import com.smoothframe.perf.GcMonitor;
import com.smoothframe.perf.ChunkRenderMonitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Lightweight diagnostics overlay. It intentionally performs no optimization itself. */
public final class PerformanceHud {
    public void render(GuiGraphicsExtractor gui, FrameTimeTracker tracker, GcMonitor gc) {
        Minecraft mc = Minecraft.getInstance();
        int x = 6;
        int y = 6;
        int white = 0xFFFFFF;
        gui.drawString(mc.font, "SmoothFrame 2.0", x, y, white, true);
        gui.drawString(mc.font, "FPS: " + mc.getFps(), x, y + 12, white, false);
        gui.drawString(mc.font, "Frame: " + round2(tracker.lastFrameMs()) + " ms", x, y + 24, white, false);
        gui.drawString(mc.font, "Avg: " + round2(tracker.averageMs()) + " ms", x, y + 36, white, false);
        gui.drawString(mc.font, "1% Low: " + round0(tracker.lowFps(0.99)) + " FPS", x, y + 48, white, false);
        gui.drawString(mc.font, "0.1% Low: " + round0(tracker.lowFps(0.999)) + " FPS", x, y + 60, white, false);
        gui.drawString(mc.font, "Worst: " + round1(tracker.worstFrameMs()) + " ms", x, y + 72, white, false);
        gui.drawString(mc.font, "Spikes: " + tracker.spikes(), x, y + 84, white, false);
        gui.drawString(mc.font, "GC: " + gc.collectionMillis() + " ms / " + gc.collections(), x, y + 96, white, false);
    }

    private static String round0(double value) { return Long.toString(Math.round(value)); }
    private static String round1(double value) { return String.format(java.util.Locale.ROOT, "%.1f", value); }
    private static String round2(double value) { return String.format(java.util.Locale.ROOT, "%.2f", value); }
}
