# SmoothFrame 2.0.0-dev status

## Chunk pipeline stage

The mod now reads the **real NeoForge 26.2 SectionRenderDispatcher** instead of creating a custom executor.

Collected values:
- compile queue size (`getCompileQueueSize()`)
- free terrain buffer count (`getFreeBufferCount()`)
- dispatcher statistics (`getStats()`)

The monitor is sampled periodically on the client/render side and is diagnostic only. It deliberately does **not** call `clearCompileQueue()`, replace Minecraft's executor, or mutate chunk tasks. Those approaches can create missing terrain or make stutter worse.

Next optimization work should use these measurements to identify queue-pressure conditions before changing scheduling behavior.
