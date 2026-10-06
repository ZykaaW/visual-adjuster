# Visual Adjuster

A client-side Fabric mod for Minecraft 1.21.11 that adjusts the first-person shield model. Choose a shield mode, then tune its height, size, width, depth, and angle from the Mod Menu screen.

## Shield modes

- **Side shield** shifts the shield outward and rotates it to a side-on pose.
- **Low shield** lowers its position.
- **Vanilla** keeps the standard shield pose.
- **Texture pack** leaves the model transform untouched for resource-pack models.

Mode adjustments can be combined with the sliders. Configuration is saved to `config/visualadjuster.json`.

## Requirements

- Minecraft 1.21.11 and Java 21+
- Fabric Loader and Fabric API
- Cloth Config API
- Mod Menu to open settings from the mod list

## Build

Use JDK 21 and Gradle 9.5.1:

```sh
./gradlew build
```

The distributable jar is in `build/libs/` (choose the jar without `-sources`). Run `./gradlew runClient` to launch a development client.
