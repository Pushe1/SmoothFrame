package com.smoothframe.debug;

import java.util.Arrays;

/**
 * Low-allocation frame-time profiler. All mutable state is owned by the client render thread.
 */
public final class FrameTimeTracker {
    private static final int MAX_SIZE = 5000;
    private final long[] samples = new long[MAX_SIZE];
    private int index;
    private int count;
    private long frameStartNanos;
    private long spikes;
    private long worstFrameNanos;
    private long lastFrameNanos;
    private double cachedAverageMs;
    private double cachedLow1Fps;
    private double cachedLow01Fps;
    private int statsAge;

    public void reset() {
        index = 0;
        count = 0;
        frameStartNanos = 0L;
        spikes = 0L;
        worstFrameNanos = 0L;
        lastFrameNanos = 0L;
        cachedAverageMs = 0.0;
        cachedLow1Fps = 0.0;
        cachedLow01Fps = 0.0;
        statsAge = 0;
    }

    public void beginFrame(long now) {
        frameStartNanos = now;
    }

    public void endFrame(long now, long spikeThresholdNanos) {
        if (frameStartNanos == 0L) {
            frameStartNanos = now;
            return;
        }

        long dt = now - frameStartNanos;
        frameStartNanos = now;
        if (dt <= 0L || dt > 5_000_000_000L) return;

        lastFrameNanos = dt;
        samples[index] = dt;
        index = (index + 1) % MAX_SIZE;
        if (count < MAX_SIZE) count++;
        if (dt > spikeThresholdNanos) spikes++;
        if (dt > worstFrameNanos) worstFrameNanos = dt;
        if (++statsAge >= 15) {
            statsAge = 0;
            recalculateCachedStats();
        }
    }

    public int count() { return count; }
    public long spikes() { return spikes; }
    public double lastFrameMs() { return lastFrameNanos / 1_000_000.0; }

    public double averageMs() {
        return cachedAverageMs;
    }

    private void recalculateCachedStats() {
        if (count == 0) {
            cachedAverageMs = 0.0;
            cachedLow1Fps = 0.0;
            cachedLow01Fps = 0.0;
            return;
        }
        long total = 0L;
        for (int i = 0; i < count; i++) total += samples[i];
        cachedAverageMs = total / (double) count / 1_000_000.0;

        long[] copy = Arrays.copyOf(samples, count);
        Arrays.sort(copy);
        cachedLow1Fps = fpsFromSorted(copy, 0.99);
        cachedLow01Fps = fpsFromSorted(copy, 0.999);
    }

    private static double fpsFromSorted(long[] sorted, double percentile) {
        int pos = (int) Math.ceil(percentile * sorted.length) - 1;
        pos = Math.max(0, Math.min(sorted.length - 1, pos));
        double frameMs = sorted[pos] / 1_000_000.0;
        return frameMs <= 0.0 ? 0.0 : 1000.0 / frameMs;
    }

    /**
     * Returns a frame-time percentile. 0.99 is the slow-frame boundary for the worst 1%.
     */
    public double percentileFrameMs(double percentile) {
        if (count == 0) return 0.0;
        double p = Math.max(0.0, Math.min(1.0, percentile));
        long[] copy = Arrays.copyOf(samples, count);
        Arrays.sort(copy);
        int pos = (int) Math.ceil(p * count) - 1;
        pos = Math.max(0, Math.min(count - 1, pos));
        return copy[pos] / 1_000_000.0;
    }

    public double lowFps(double percentile) {
        if (percentile >= 0.999) return cachedLow01Fps;
        if (percentile >= 0.99) return cachedLow1Fps;
        double frameMs = percentileFrameMs(percentile);
        return frameMs <= 0.0 ? 0.0 : 1000.0 / frameMs;
    }

    public double worstFrameMs() {
        return worstFrameNanos / 1_000_000.0;
    }
}
