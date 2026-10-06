package com.smoothframe.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class SmoothFrameConfig {
    public static final String VERSION = "2.0.0";
    public static final ModConfigSpec CLIENT_SPEC;

    public static final ModConfigSpec.BooleanValue PROFILER;
    public static final ModConfigSpec.BooleanValue HUD;
    public static final ModConfigSpec.BooleanValue LOG_SPIKES;
    public static final ModConfigSpec.IntValue SPIKE_THRESHOLD_MS;
    public static final ModConfigSpec.IntValue HISTORY_SIZE;
    public static final ModConfigSpec.BooleanValue NAME_TAG_CULLING;
    public static final ModConfigSpec.IntValue NAME_TAG_DISTANCE;
    public static final ModConfigSpec.BooleanValue LIVING_ENTITY_CULLING;
    public static final ModConfigSpec.IntValue LIVING_ENTITY_DISTANCE;
    public static final ModConfigSpec.BooleanValue CHUNK_TELEMETRY;
    public static final ModConfigSpec.IntValue CHUNK_SAMPLE_INTERVAL;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();
        b.push("profiler");
        PROFILER = b.comment("Collect frame-time samples. Very low overhead.")
                .define("enabled", true);
        HUD = b.comment("Show the SmoothFrame performance overlay.")
                .define("hud", false);
        LOG_SPIKES = b.comment("Log unusually long frames to latest.log.")
                .define("logSpikes", true);
        SPIKE_THRESHOLD_MS = b.comment("Frame duration at which a spike is recorded.")
                .defineInRange("spikeThresholdMs", 33, 16, 500);
        HISTORY_SIZE = b.comment("Number of recent frame samples kept for percentiles.")
                .defineInRange("historySize", 1200, 120, 5000);
        NAME_TAG_CULLING = b.comment("Skip distant entity name-tags. This does not affect entity simulation.")
                .define("nameTagCulling", true);
        NAME_TAG_DISTANCE = b.comment("Maximum distance in blocks at which an entity name-tag is rendered.")
                .defineInRange("nameTagDistance", 64, 8, 256);
        LIVING_ENTITY_CULLING = b.comment("Skip rendering distant living entities. Does not affect simulation or ticking.")
                .define("livingEntityCulling", true);
        LIVING_ENTITY_DISTANCE = b.comment("Maximum distance in blocks at which living entities are rendered.")
                .defineInRange("livingEntityDistance", 128, 16, 512);
        CHUNK_TELEMETRY = b.comment("Read NeoForge 26.2 section-render queue statistics. Diagnostic only; it never clears or replaces Minecraft queues.")
                .define("chunkTelemetry", true);
        CHUNK_SAMPLE_INTERVAL = b.comment("How many rendered frames between chunk queue samples.")
                .defineInRange("chunkSampleInterval", 10, 1, 120);
        b.pop();
        CLIENT_SPEC = b.build();
    }

    private SmoothFrameConfig() {}
}
