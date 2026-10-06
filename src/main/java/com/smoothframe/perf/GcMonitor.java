package com.smoothframe.perf;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/** Observes JVM GC counters without forcing GC or allocating on every frame. */
public final class GcMonitor {
    private final List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
    private long lastCollectionCount;
    private long lastCollectionTime;
    private long collections;
    private long collectionMillis;

    public GcMonitor() {
        snapshot();
    }

    public void sample() {
        long count = 0L;
        long time = 0L;
        for (GarbageCollectorMXBean bean : beans) {
            long c = bean.getCollectionCount();
            long t = bean.getCollectionTime();
            if (c > 0) count += c;
            if (t > 0) time += t;
        }
        collections = Math.max(0L, count - lastCollectionCount);
        collectionMillis = Math.max(0L, time - lastCollectionTime);
        lastCollectionCount = count;
        lastCollectionTime = time;
    }

    private void snapshot() {
        lastCollectionCount = 0L;
        lastCollectionTime = 0L;
        for (GarbageCollectorMXBean bean : beans) {
            long c = bean.getCollectionCount();
            long t = bean.getCollectionTime();
            if (c > 0) lastCollectionCount += c;
            if (t > 0) lastCollectionTime += t;
        }
    }

    public long collections() { return collections; }
    public long collectionMillis() { return collectionMillis; }
}
