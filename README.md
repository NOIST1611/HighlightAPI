# Highlight API

A Minecraft Forge 1.20.1 library mod that adds support for custom highlights and outlines for blocks, entities and regions.

## Features
- Outline and fill rendering for blocks, entities and AABB regions
- Region highlighting between two points
- Built-in animations (Pulse, Blink, Fade In)
- Custom animation support
- Depth modes (hide behind blocks or show through)
- Eternal and delayed lifetime support

## Installation
Requires [Forge 1.20.1](https://files.minecraftforge.net/)

Add to your `build.gradle`:

### Modrinth Maven
```gradle
repositories {
    maven { url "https://api.modrinth.com/maven" }
}

dependencies {
    implementation fg.deobf("maven.modrinth:highlight-api:1.0.0")
}
```

### JitPack
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation fg.deobf("com.github.NOIST1611:HighlightAPI:v1.0.0")
}
```

## Usage

### Basic highlight on a block
```java
HighlightHandle handle = HighlightAPI.create(Lifetime.ETERNAL)
    .setTarget(new BlockPos(0, 64, 0))
    .setOutlineColor(1.0f, 0.0f, 0.0f, 1.0f)
    .setFillColor(1.0f, 0.0f, 0.0f, 0.3f)
    .register();

// Remove later
handle.remove();
```

### Highlight on an entity
```java
HighlightAPI.create(Lifetime.ETERNAL)
    .setTarget(entity)
    .setOutlineColor(0.0f, 1.0f, 0.0f, 1.0f)
    .setFillColor(0.0f, 1.0f, 0.0f, 0.2f)
    .register();
```

### Region highlight
```java
HighlightAPI.create(Lifetime.ETERNAL)
    .setRenderMode(RenderMode.REGION)
    .setTarget(new BlockPos(0, 64, 0))
    .setTargetEnd(new BlockPos(10, 70, 10))
    .setOutlineColor(0.0f, 0.0f, 1.0f, 1.0f)
    .setFillColor(0.0f, 0.0f, 1.0f, 0.2f)
    .register();
```

### Delayed highlight
```java
HighlightAPI.create(Lifetime.DELAYED, 5.0f)
    .setTarget(new BlockPos(0, 64, 0))
    .setOutlineColor(1.0f, 1.0f, 0.0f, 1.0f)
    .register();
```

### Animations
```java
// Built-in preset
HighlightAPI.create(Lifetime.ETERNAL)
    .setTarget(blockPos)
    .setAnimation(AnimationType.PULSE)
    .register();

// Custom animation
HighlightAPI.create(Lifetime.ETERNAL)
    .setTarget(blockPos)
    .setAnimation(new ICustomAnimation() {
        private float time = 0.0f;

        @Override
        public float tick(float partialTick) {
            time += partialTick * 0.05f;
            return (float)(Math.sin(time) * 0.5 + 0.5);
        }
    })
    .register();
```

### Depth mode
```java
// Visible through blocks
HighlightAPI.create(Lifetime.ETERNAL)
    .setTarget(blockPos)
    .setDepthMode(DepthMode.IGNORE)
    .register();
```

### Modifying after creation
```java
HighlightHandle handle = HighlightAPI.create(Lifetime.ETERNAL)
    .setTarget(blockPos)
    .register();

handle.setOutlineColor(0.0f, 1.0f, 0.0f, 1.0f);
handle.setVisible(false);
handle.remove();
```

## Links
- [Modrinth](https://modrinth.com/mod/highlight-api)
- [GitHub](https://github.com/NOIST1611/HighlightAPI)
- [Report Issues](https://github.com/NOIST1611/HighlightAPI/issues)

## License
MIT