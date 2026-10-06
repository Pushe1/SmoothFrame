# SmoothFrame 2.0.0 — build notes

Target: Minecraft 26.2 / NeoForge 26.2.0.87 / Java 25.

The project follows the official NeoForge 26.2 ModDevGradle template. The local execution environment used to prepare this package does not contain JDK 25 or Gradle, so this package has not been falsely labeled as a locally verified JAR.

For a real build, run:

```text
gradle --no-daemon build
```

The included GitHub Actions workflow builds with Temurin 25 and Gradle 8.8 and uploads the resulting JAR as an artifact.
